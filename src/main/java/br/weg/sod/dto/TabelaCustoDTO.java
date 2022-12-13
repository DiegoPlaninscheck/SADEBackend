package br.weg.sod.dto;

import br.weg.sod.model.entities.CentroCustoPagante;
import br.weg.sod.model.entities.LinhaTabela;
import br.weg.sod.model.entities.Proposta;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Setter
@Getter
public class TabelaCustoDTO {

    @NotBlank
    private String tituloTabela;

    @Digits(integer = 5, fraction = 0)
    private Integer quantidadeTotal;

    @Positive
    private Double valorTotal;

    private Boolean licenca;

    @NotNull
    private Proposta proposta;

    private List<CentroCustoPaganteDTO> centrosCustoPagantes;

    private List<LinhaTabelaDTO> linhasTabela;
}
