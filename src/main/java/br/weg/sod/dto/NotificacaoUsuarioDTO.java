package br.weg.sod.dto;

import br.weg.sod.model.entities.Notificacao;
import br.weg.sod.model.entities.Usuario;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class NotificacaoUsuarioDTO {

    @NotNull
    private Notificacao notificacao;

    @NotNull
    private Usuario usuario;

}
