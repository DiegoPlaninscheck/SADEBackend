package br.weg.sod.dto;

import br.weg.sod.model.entities.ATA;
import br.weg.sod.model.entities.Pauta;
import br.weg.sod.model.entities.Proposta;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
public class DecisaoPropostaPautaEdicaoDTO {

    @NotNull
    private Proposta proposta;

    @NotNull
    private StatusDemanda statusDemandaComissao;

    @NotNull
    private Boolean ataPublicada;

    private String comentario;
}
