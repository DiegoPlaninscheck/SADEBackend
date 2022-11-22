package br.weg.sod.dto;

import br.weg.sod.model.entities.Proposta;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class TabelaCustoDTO {

    @NotBlank
    private String tituloTabela;

    @Digits(integer = 5, fraction = 0)
    private Integer quantidadeTotal;

    @Positive
    private Double valorTotal;

    @NotNull
    private Proposta proposta;
}
