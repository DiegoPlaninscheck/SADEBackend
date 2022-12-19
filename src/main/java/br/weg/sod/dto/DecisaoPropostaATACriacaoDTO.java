package br.weg.sod.dto;

import br.weg.sod.model.entities.Proposta;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DecisaoPropostaATACriacaoDTO {

    @NotNull
    private Proposta proposta;
}
