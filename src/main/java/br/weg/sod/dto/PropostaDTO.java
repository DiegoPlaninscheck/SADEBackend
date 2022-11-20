package br.weg.sod.dto;

import lombok.NonNull;

import java.util.Date;

public class PropostaDTO {

    @NonNull
    private Integer idProposta;

    @NonNull
    private String escopo;

    @NonNull
    private Double payback;

    @NonNull
    private Date periodoExecucaoInicio;

    @NonNull
    private Date periodoExecucaoFim;

    @NonNull
    private Boolean aprovadoWorkflow;

    @NonNull
    private Boolean emWorkflow;

    @NonNull
    private Integer idDemanda;

    @NonNull
    private Integer propostaIdProposta;
}
