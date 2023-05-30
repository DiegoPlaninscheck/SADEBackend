package br.weg.sade.dto;

import br.weg.sade.model.entities.Usuario;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ArquivoDemandaDTO {

    @NotNull
    private byte[] arquivo;

    @NotNull
    private Usuario usuario;
}
