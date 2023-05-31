package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Proposta;
import br.weg.sade.model.enuns.StatusDemanda;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Data
public class DecisaoPropostaATADTO {

    @Digits(integer = 15, fraction = 0)
    private Long numeroSequencial;

    @NotNull
    private StatusDemanda statusDemandaComissao;

    @NotNull
    private Proposta proposta;

    private String comentario;
}
