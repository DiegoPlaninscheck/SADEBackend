package br.weg.sade.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
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

    @NotNull
    private Boolean licenca;

    @Size
    private List<CentroCustoPaganteDTO> centrosCustoPagantes;

    @Size
    private List<LinhaTabelaDTO> linhasTabela;
}
