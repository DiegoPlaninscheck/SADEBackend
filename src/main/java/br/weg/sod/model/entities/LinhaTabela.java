package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class LinhaTabela {

    @Id
    @Column
    private Integer idLinhaTabela;

    @Column(nullable = false)
    private String recurso;

    @Column(nullable = false)
    private Integer valor;

    @Column(nullable = false)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "idTabelaCusto", nullable = false)
    private Integer idTabelaCusto;

}
