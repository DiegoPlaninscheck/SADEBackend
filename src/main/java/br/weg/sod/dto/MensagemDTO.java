package br.weg.sod.dto;

import lombok.NonNull;

import java.sql.Timestamp;

public class MensagemDTO {

    @NonNull
    private Integer idMensagem;

    @NonNull
    private String mensagem;

    @NonNull
    private byte[] arquivo;

    @NonNull
    private Timestamp dataHoraMensagem;

    @NonNull
    private Integer idChat;

    @NonNull
    private Integer idUsuario;
}
