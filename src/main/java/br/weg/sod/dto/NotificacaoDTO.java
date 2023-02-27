package br.weg.sod.dto;


import br.weg.sod.model.entities.Usuario;
import br.weg.sod.model.entities.enuns.AcaoNotificacao;
import br.weg.sod.model.entities.enuns.TipoNotificacao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
