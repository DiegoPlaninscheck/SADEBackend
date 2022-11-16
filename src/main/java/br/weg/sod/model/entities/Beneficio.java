package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Beneficio {

    @Id
    @Column
    private Integer idBeneficio;

    @Column(nullable = false)
    private TipoBeneficio tipoBeneficio;

    @Column(nullable = false)
    private String descricao;

    @Column
    private Moeda moeda;

    @Column
    private Double memoriaCalculo;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Integer idDemanda;
}
