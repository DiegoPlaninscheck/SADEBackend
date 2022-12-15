package br.weg.sod.dto;

import br.weg.sod.model.entities.CentroCustoPagante;
import br.weg.sod.model.entities.LinhaTabela;
import br.weg.sod.model.entities.Proposta;
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

//    @Size
    private List<CentroCustoPagante> centrosCustoPagantes;

//    @Size
    private List<LinhaTabela> linhasTabela;
}
