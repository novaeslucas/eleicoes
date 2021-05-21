package br.com.cortex.eleicoes.repository;

import br.com.cortex.eleicoes.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    @Query("select m from Municipio m where m.estado.codigo = ?1")
    List<Municipio> obterPorCodigoEstado(String sigla);

    @Query("select m from Municipio m where m.estado.sigla = ?1")
    List<Municipio> obterPorSiglaEstado(String sigla);

}
