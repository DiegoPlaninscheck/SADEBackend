package br.weg.sod.dto;

import br.weg.sod.model.entities.CentroCusto;
import br.weg.sod.model.entities.Demanda;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class CentroCustoDemandaDTO {

    @Positive
    private Double porcentagemDespesa;

    @NotNull
    private Demanda demanda;

    @NotNull
    private CentroCusto centroCusto;
}
