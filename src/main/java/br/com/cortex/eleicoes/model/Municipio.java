package br.com.cortex.eleicoes.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Municipio {

    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private String codigo;
    @ManyToOne
    @JoinColumn(name="estadoId")
    private Estado estado;
    @Lob
    @Column(columnDefinition = "CLOB")
    private String candidatos;
    @Lob
    @Column(columnDefinition = "CLOB")
    private String malhas;

}
