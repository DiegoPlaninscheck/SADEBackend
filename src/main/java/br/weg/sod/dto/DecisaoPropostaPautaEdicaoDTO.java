package br.weg.sod.dto;

import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DecisaoPropostaPautaEdicaoDTO extends DecisaoPropostaPautaCriacaoDTO{

    @NotNull
    private StatusDemanda statusDemandaComissao;

    @NotNull
    private Boolean ataPublicada;

    private String comentario;
}
