package br.weg.sod.dto;

import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.enuns.Moeda;
import br.weg.sod.model.entities.enuns.TipoBeneficio;
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
