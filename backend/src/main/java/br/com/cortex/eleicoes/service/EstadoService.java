package br.com.cortex.eleicoes.service;

import br.com.cortex.eleicoes.model.Estado;
import br.com.cortex.eleicoes.repository.EstadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {

    private final EstadoRepository repository;

    public EstadoService(EstadoRepository repository){
        this.repository = repository;
    }

    public Estado cadastrar(Estado estado){
        return repository.save(estado);
    }

    public List<Estado> obterTodos(){
        return repository.findAll();
    }

    public Estado obterPorSigla(String sigla){
        return repository.obterPorSigla(sigla);
    }

    public int obterQuantidadeEstados(){
        return repository.obterQuantidadeEstados();
    }

}
