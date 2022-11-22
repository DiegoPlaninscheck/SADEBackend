package br.weg.sod.dto;

import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.enuns.Moeda;
import br.weg.sod.model.entities.enuns.TipoBeneficio;
import lombok.Getter;


import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class BeneficioDTO {

    @Digits(integer = 10, fraction = 0)
    private Integer idBeneficio;

    @NotNull
    private TipoBeneficio tipoBeneficio;

    @NotBlank
    private String descricao;

    private Moeda moeda;

    private String memoriaCalculo;

    private Double valor;

    @NotNull
    private Demanda demanda;

}
