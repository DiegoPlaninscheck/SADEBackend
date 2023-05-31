package br.weg.sade.model.dto;

import br.weg.sade.model.entity.AnalistaTI;
import br.weg.sade.model.entity.Usuario;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class ForumDTO {

    @NotNull
    private String nomeForum;

    @NotNull
    private AnalistaTI analistaResponsavel;

   @NotNull
    private List<Usuario> usuariosForum;
}
