package br.weg.sod.dto;

import lombok.NonNull;

public class NotificacaoDTO {

    @NonNull
    private Integer idNotificacao;

    @NonNull
    private String notificacao;

    @NonNull
    private String link;

    @NonNull
    private Integer notificacaoIdNotificacao;
}
