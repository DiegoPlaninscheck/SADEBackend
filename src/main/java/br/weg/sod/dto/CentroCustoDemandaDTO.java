package br.weg.sod.dto;

import lombok.NonNull;

public class CentroCustoDemandaDTO {

    @NonNull
    private Double porcentagemDespesa;

    @NonNull
    private Integer idDemanda;

    @NonNull
    private Integer idCentroCusto;
}
