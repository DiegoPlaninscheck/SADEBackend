package br.weg.sade.dto;

import br.weg.sade.model.entities.Chat;
import br.weg.sade.model.entities.Usuario;
import lombok.Getter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
public class MensagemDTO {

    @NotBlank
    private String mensagem;

    @FutureOrPresent
    private Timestamp dataHoraMensagem;

    @NotNull
    private Chat chat;

    @NotNull
    private Usuario usuario;

    private byte[] arquivo;
}
