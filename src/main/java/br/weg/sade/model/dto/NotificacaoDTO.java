package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Usuario;
import br.weg.sade.model.enums.AcaoNotificacao;
import br.weg.sade.model.enums.TipoNotificacao;
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
