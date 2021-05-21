package br.com.cortex.eleicoes.controller;

import br.com.cortex.eleicoes.model.Estado;
import br.com.cortex.eleicoes.model.Municipio;
import br.com.cortex.eleicoes.service.EstadoService;
import br.com.cortex.eleicoes.service.MunicipioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/eleicao/2014/presidente/primeiro-turno")
public class PresidentePrimeiroTurno2014 {

    private static final String REGEX_INTEGER = "[0-9]*";

    private final EstadoService estadoService;

    private final MunicipioService municipioService;

    public PresidentePrimeiroTurno2014(EstadoService estadoService, MunicipioService municipioService) {
        this.estadoService = estadoService;
        this.municipioService = municipioService;
    }

    @GetMapping("/estados")
    public List<Estado> getVotosAgregadosTodosEstados(){
        return estadoService.obterTodos();
    }

    @GetMapping("/estados/{estado}/municipios")
    public List<Municipio> getVotosAgregadosPorMunicipioEstado(@PathVariable(value = "estado") String estado){
        List<Municipio> municipios;
        if(isInteger(estado)){
            municipios = municipioService.obterPorEstado(estado, "codigo_estado");
        }else{
            municipios = municipioService.obterPorEstado(estado, "sigla_estado");
        }
        return municipios;
    }

    private static boolean isInteger(String str) {
        return str != null && str.matches(REGEX_INTEGER);
    }
}
