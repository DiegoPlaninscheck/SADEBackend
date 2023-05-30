package br.weg.sade.dto;

import br.weg.sade.model.entities.Proposta;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
public class DecisaoPropostaPautaCriacaoDTO {

    @NotNull
    private Proposta proposta;
}
