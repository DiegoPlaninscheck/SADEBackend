package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Demanda;
import br.weg.sade.model.entity.Usuario;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatDTO {

    @NotNull
    private Boolean ativo;

    @NotNull
    private Demanda demanda;

    private List<Usuario> usuariosChat = new ArrayList<>();
}
