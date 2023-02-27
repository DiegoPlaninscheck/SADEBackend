package br.weg.sod.dto;

import br.weg.sod.model.entities.Proposta;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
public class DecisaoPropostaPautaCriacaoDTO {

    @NotNull
    private Proposta proposta;
}
