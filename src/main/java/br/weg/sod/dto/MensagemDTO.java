package br.weg.sod.dto;

import br.weg.sod.model.entities.Chat;
import br.weg.sod.model.entities.Usuario;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
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
