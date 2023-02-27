package br.weg.sod.dto;

import br.weg.sod.model.entities.AnalistaTI;
import br.weg.sod.model.entities.Usuario;
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
