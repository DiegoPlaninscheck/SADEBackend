package br.weg.sade.dto;

import br.weg.sade.model.entities.Demanda;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ChatDTO {

    @NotNull
    private Boolean ativo;

    @NotNull
    private Demanda demanda;
}
