package br.com.cortex.eleicoes;

import br.com.cortex.eleicoes.model.Estado;
import br.com.cortex.eleicoes.model.Municipio;
import br.com.cortex.eleicoes.service.EstadoService;
import br.com.cortex.eleicoes.service.MunicipioService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SpringBootApplication
public class EleicoesApplication {

	private static final String ARQUIVO_JSON = "data/resultado-1-turno-presidente-2014.json";
	private static final String API_IBGE_LOCALIDADES = "https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome";
	private static final String API_IBGE_MALHAS = "https://servicodados.ibge.gov.br/api/v2/malhas/";
	private static final String PARAMETRO_MALHAS = "?formato=application/vnd.geo+json";
	private static final String UF = "UF";
	private static final String MUNICIPIO = "MU";

	public static void main(String[] args) {
		ApplicationContext appContext = SpringApplication.run(EleicoesApplication.class, args);
		int quantidadeEstados = appContext.getBean(EstadoService.class).obterQuantidadeEstados();
		if(quantidadeEstados == 0){
			carregarDados(appContext);
		}
	}

	private static void carregarDados(ApplicationContext appContext){
		JSONParser jsonParser = new JSONParser();
		ClassPathResource resource = new ClassPathResource(ARQUIVO_JSON);
		try {
			InputStream is = resource.getInputStream();
			Object obj = jsonParser.parse(new InputStreamReader(is));
			JSONArray eleicoes2014 = (JSONArray) obj;
			for (Object item : eleicoes2014) {
				JSONArray itensEleicoes2014 = (JSONArray) item;
				System.out.println(((JSONArray) item).toJSONString());
				if(UF.equals(itensEleicoes2014.get(0))){
					//inserir Estado
					importarEstados(itensEleicoes2014, appContext.getBean(EstadoService.class));
				}else if(MUNICIPIO.equals(itensEleicoes2014.get(2))){
					//inserir Municipio
					importarMunicipios(itensEleicoes2014, appContext.getBean(MunicipioService.class));
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private static void importarEstados(JSONArray itensEstados, EstadoService service){
		Estado novoEstado = new Estado();
		novoEstado.setNome((String) itensEstados.get(0));
		novoEstado.setSigla((String) itensEstados.get(1));
		novoEstado.setCodigo(obterCodigoEstado(novoEstado.getSigla()));
		if(novoEstado.getCodigo() != null){
			novoEstado.setMalhas(obterMalhas(novoEstado.getCodigo()).toJSONString());
		}
		JSONArray candidatosEstado = new JSONArray();
		for(int i = 4; i < itensEstados.size(); i++){
			candidatosEstado.add(itensEstados.get(i));
		}
		novoEstado.setCandidatos(candidatosEstado.toJSONString());
		service.cadastrar(novoEstado);
	}

	private static void importarMunicipios(JSONArray itensMunicipio, MunicipioService service){
		Municipio novoMunicipio = new Municipio();
		novoMunicipio.setNome((String) itensMunicipio.get(0));
		novoMunicipio.setCodigo((String) itensMunicipio.get(1));
		novoMunicipio.setEstado(service.obterEstado((String) itensMunicipio.get(5)));

		if(novoMunicipio.getCodigo() != null){
			novoMunicipio.setMalhas(obterMalhas(novoMunicipio.getCodigo()).toJSONString());
		}

		JSONArray candidatosMunicipios = new JSONArray();
		for(int i = 4; i < itensMunicipio.size(); i++){
			candidatosMunicipios.add(itensMunicipio.get(i));
		}
		novoMunicipio.setCandidatos(candidatosMunicipios.toJSONString());
		service.cadastrar(novoMunicipio);
	}

	private static String obterCodigoEstado(String siglaEstado){
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(API_IBGE_LOCALIDADES, String.class);
		JSONParser parser = new JSONParser();
		String codigoEstado = null;
		try {
			JSONArray json = (JSONArray) parser.parse(response);
			for (Object item : json) {
				JSONObject estadoIbge = (JSONObject) item;
				if(siglaEstado.equals(estadoIbge.get("sigla"))){
					codigoEstado = Long.toString((Long) estadoIbge.get("id"));
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return codigoEstado;
	}

	private static JSONObject obterMalhas(String codigo) {
		JSONObject jsonobj = new JSONObject();
		try{
			JSONParser parser = new JSONParser();
			RestTemplate restTemplate = new RestTemplate();
			String linkApi = API_IBGE_MALHAS + codigo + PARAMETRO_MALHAS;
			String retorno = restTemplate.getForObject(linkApi, String.class);
			jsonobj = (JSONObject) parser.parse(retorno);
		} catch (ParseException e){
			e.printStackTrace();
		}
		return jsonobj;
	}

}
