package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class TabelaCusto {

    @Id
    @Column
    private Integer idTabelaCusto;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer valor;

    @Column(nullable = false)
    private Double total;

    @ManyToOne
    @JoinColumn(name =  "idProposta", nullable = false)
    private Integer idProposta;
}
