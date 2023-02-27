package br.weg.sod.dto;

import br.weg.sod.model.entities.Proposta;
import br.weg.sod.model.entities.enuns.StatusDemanda;
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
