package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "linhaTabela")
public class LinhaTabela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idLinhaTabela;

    @Column(nullable = false)
    private String nomeRecurso;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Double valorQuantidade;

}
