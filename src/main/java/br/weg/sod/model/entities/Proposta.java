package br.weg.sod.model.entities;

import javax.persistence.*;
import java.util.Date;

public class Proposta {

    @Id
    @Column
    private Integer idProposta;

    @Column(nullable = false)
    private String escopo;

    @Column(nullable = false)
    private Double payback;

    @Column(nullable = false)
    private Date periodoExecucaoInicio;

    @Column(nullable = false)
    private Date periodoExecucaoFim;

    @Column(nullable = false)
    private Boolean aprovadoWorkflow;

    @Column(nullable = false)
    private Boolean emWorkflow;

    @OneToOne
    private Integer idDemanda;

    @OneToMany
    @JoinColumn(name = "propostaIdProposta", nullable = false)
    private Integer responsavelNegocioIdProposta;

}
