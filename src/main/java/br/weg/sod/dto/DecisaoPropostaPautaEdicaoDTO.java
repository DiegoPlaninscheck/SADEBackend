package br.weg.sod.dto;

import br.weg.sod.model.entities.Proposta;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Data
public class DecisaoPropostaPautaEdicaoDTO {

    @Digits(integer = 10, fraction = 0)
    private Integer idDecisaoPropostaPauta;

    @NotNull
    private StatusDemanda statusDemandaComissao;

    @NotNull
    private Boolean ataPublicada;

    private Proposta proposta;

    private String comentario;
}
