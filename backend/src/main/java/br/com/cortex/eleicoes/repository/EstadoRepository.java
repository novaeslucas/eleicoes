package br.com.cortex.eleicoes.repository;

import br.com.cortex.eleicoes.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    @Query("select e from Estado e where e.sigla = ?1")
    Estado obterPorSigla(String sigla);

    @Query("select count(e) from Estado e")
    int obterQuantidadeEstados();

}
