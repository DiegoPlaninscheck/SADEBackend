package br.weg.sade.model.dto;

import br.weg.sade.model.enums.Moeda;
import br.weg.sade.model.enums.TipoBeneficio;
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
