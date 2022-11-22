package br.weg.sod.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class NotificacaoDTO {

    @NotBlank
    private String tituloNotificacao;

    @NotBlank
    private String descricaoNotificacao;

    @NotBlank
    private String linkNotificacao;

}
