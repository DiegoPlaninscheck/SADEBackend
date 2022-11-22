package br.weg.sod.dto;

import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.Usuario;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
public class ArquivoDemandaDTO {

    @Digits(integer = 10, fraction = 0)
    private Integer idArquivoDemanda;

    @NotNull
    private byte[] arquivo;

    @NotNull
    private Demanda demanda;

    @NotNull
    private Usuario usuario;
}
