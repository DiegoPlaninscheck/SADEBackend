package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class CentroCustoDemanda {

    @Column
    private Double porcentagemDespesa;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Integer idDemanda;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private Integer idCentroCusto;

}
