package br.weg.sod.dto;

import br.weg.sod.model.entities.enuns.StatusDemanda;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;


public class DecisaoPropostaATAEdicaoDTO {

    @Digits(integer = 15, fraction = 0)
    private Integer numeroSequencial;

    @NotNull
    private StatusDemanda statusDemandaComissao;

    private String comentario;
}
