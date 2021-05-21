package br.com.cortex.eleicoes.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Estado {

    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private String sigla;
    private String codigo;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String malhas;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String candidatos;

}
