package br.weg.sod.dto;

import br.weg.sod.model.entities.Demanda;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
public class ChatDTO {

    @NotNull
    private Boolean ativo;

    @NotNull
    private Demanda demanda;
}
