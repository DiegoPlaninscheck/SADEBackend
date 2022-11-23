package br.weg.sod.dto;

import br.weg.sod.model.entities.TabelaCusto;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class LinhaTabelaDTO {

    @NotBlank
    private String nomeRecurso;

    @Digits(integer = 4, fraction = 0)
    private Integer quantidade;

    @Positive
    private Double valorHora;

    @NotNull
    private TabelaCusto tabelaCusto;
}