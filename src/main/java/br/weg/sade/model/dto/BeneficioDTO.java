package br.weg.sade.model.dto;

import br.weg.sade.model.enuns.Moeda;
import br.weg.sade.model.enuns.TipoBeneficio;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class BeneficioDTO {

    @NotNull
    private TipoBeneficio tipoBeneficio;

    @NotBlank
    private String descricao;

    private Moeda moeda;

    private Double valor;

}
