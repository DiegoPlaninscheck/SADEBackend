package br.weg.sod.dto;

import br.weg.sod.model.entities.Pauta;
import br.weg.sod.model.entities.Proposta;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DecisaoPropostaPautaCriacaoDTO {

    @NotNull
    private Proposta proposta;
}
