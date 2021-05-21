package br.com.cortex.eleicoes.service;

import br.com.cortex.eleicoes.model.Estado;
import br.com.cortex.eleicoes.model.Municipio;
import br.com.cortex.eleicoes.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipioService {

    private MunicipioRepository repository;

    @Autowired
    private EstadoService estadoService;

    public MunicipioService(MunicipioRepository repository){
        this.repository = repository;
    }

    public Municipio cadastrar(Municipio municipio){
        return repository.save(municipio);
    }

    public List<Municipio> obterTodos(){
        return repository.findAll();
    }

    public Estado obterEstado(String siglaEstado){
        return estadoService.obterPorSigla(siglaEstado);
    }

    public List<Municipio> obterPorEstado(String estado, String tipoCodigo){
        if(tipoCodigo.equals("codigo_estado")){
            return repository.obterPorCodigoEstado(estado);
        }else{
            return repository.obterPorSiglaEstado(estado);
        }

    }

}
