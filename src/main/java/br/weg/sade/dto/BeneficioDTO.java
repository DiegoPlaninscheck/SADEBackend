package br.weg.sade.dto;

import br.weg.sade.model.entities.enuns.Moeda;
import br.weg.sade.model.entities.enuns.TipoBeneficio;
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
