package br.weg.sade.model.dto;

import br.weg.sade.model.entity.CentroCusto;
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
