package br.weg.sade.dto;

import br.weg.sade.model.entities.CentroCusto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
public class CentroCustoPaganteDTO {

    @Positive
    private Double porcentagemDespesa;

    @NotNull
    private CentroCusto centroCusto;

}
