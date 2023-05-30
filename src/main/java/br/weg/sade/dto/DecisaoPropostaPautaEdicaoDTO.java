package br.weg.sade.dto;

import br.weg.sade.model.entities.Proposta;
import br.weg.sade.model.entities.enuns.StatusDemanda;
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
