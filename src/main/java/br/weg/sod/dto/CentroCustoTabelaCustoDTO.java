package br.weg.sod.dto;

import br.weg.sod.model.entities.CentroCusto;
import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.TabelaCusto;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class CentroCustoTabelaCustoDTO {

    @Positive
    private Double porcentagemDespesa;

    @NotNull
    private TabelaCusto tabelaCusto;

    @NotNull
    private CentroCusto centroCusto;
}
