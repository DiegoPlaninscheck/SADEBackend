package br.weg.sod.dto;

import br.weg.sod.model.entities.enuns.Moeda;
import br.weg.sod.model.entities.enuns.TipoBeneficio;
import lombok.NonNull;

public class BeneficioDTO {

    @NonNull
    private Integer idBeneficio;

    @NonNull
    private TipoBeneficio tipoBeneficio;

    @NonNull
    private String descricao;

    @NonNull
    private Moeda moeda;

    @NonNull
    private Double memoriaCalculo;

    @NonNull
    private Integer idDemanda;

}
