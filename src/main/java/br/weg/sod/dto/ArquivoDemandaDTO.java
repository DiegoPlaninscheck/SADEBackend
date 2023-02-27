package br.weg.sod.dto;

import br.weg.sod.model.entities.Usuario;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ArquivoDemandaDTO {

    @NotNull
    private byte[] arquivo;

    @NotNull
    private Usuario usuario;
}
