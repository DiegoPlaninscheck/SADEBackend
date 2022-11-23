package br.weg.sod.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CentroCustoDTO {

    @NotBlank
    private String nomeCentroCusto;
}
