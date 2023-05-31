package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Usuario;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ArquivoDemandaDTO {

    @NotNull
    private byte[] arquivo;

    @NotNull
    private Usuario usuario;
}
