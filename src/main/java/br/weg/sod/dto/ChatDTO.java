package br.weg.sod.dto;

import lombok.NonNull;

public class ChatDTO {

    @NonNull
    private Integer idChat;

    @NonNull
    private Boolean ativo;

    @NonNull
    private Integer idDemanda;
}
