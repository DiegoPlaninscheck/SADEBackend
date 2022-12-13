package br.weg.sod.dto;

import br.weg.sod.model.entities.Usuario;
import br.weg.sod.model.entities.enuns.TipoNotificacao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
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

    @NotBlank
    private TipoNotificacao tipoNotificacao;

    @Digits(integer = 5, fraction = 0)
    private Integer idComponenteLink;

    @NotBlank
    private List<Usuario> usuariosRelacionados;

}
