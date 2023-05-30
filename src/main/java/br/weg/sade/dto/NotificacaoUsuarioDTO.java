package br.weg.sade.dto;

import br.weg.sade.model.entities.Notificacao;
import br.weg.sade.model.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class NotificacaoUsuarioDTO {

    @NotNull
    private Notificacao notificacao;

    @NotNull
    private Usuario usuario;

}
