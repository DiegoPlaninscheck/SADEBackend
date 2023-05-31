package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Demanda;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ChatDTO {

    @NotNull
    private Boolean ativo;

    @NotNull
    private Demanda demanda;
}
