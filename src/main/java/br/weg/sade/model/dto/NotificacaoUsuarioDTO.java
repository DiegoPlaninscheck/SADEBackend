package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Notificacao;
import br.weg.sade.model.entity.Usuario;
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
