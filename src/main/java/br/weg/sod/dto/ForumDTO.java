package br.weg.sod.dto;

import br.weg.sod.model.entities.Usuario;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class ForumDTO {

    @NotNull
    private String nomeForum;

    @NotNull
    private Integer analistaIdUsuario;

   @NotNull
    private List<Usuario> usuariosForum;
}
