package br.weg.sade.dto;

import br.weg.sade.model.entities.Usuario;
import br.weg.sade.model.entities.enuns.AcaoNotificacao;
import br.weg.sade.model.entities.enuns.TipoNotificacao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Getter
public class NotificacaoDTO {

    @NotBlank
    private String tituloNotificacao;

    @NotBlank
    private String descricaoNotificacao;

    @NotBlank
    private String linkNotificacao;
    @NotNull
    private TipoNotificacao tipoNotificacao;

    @NotNull
    private AcaoNotificacao acao;

    @Digits(integer = 5, fraction = 0)
    private Integer idComponenteLink;

    @NotNull
    private List<Usuario> usuariosRelacionados;

}
