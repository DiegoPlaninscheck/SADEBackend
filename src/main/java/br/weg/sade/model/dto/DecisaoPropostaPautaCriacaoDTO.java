package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Proposta;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
public class DecisaoPropostaPautaCriacaoDTO {

    @NotNull
    private Proposta proposta;
}
