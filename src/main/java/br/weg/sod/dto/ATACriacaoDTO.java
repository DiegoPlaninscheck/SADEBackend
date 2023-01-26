package br.weg.sod.dto;


import br.weg.sod.model.entities.Pauta;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
public class ATACriacaoDTO {

    @NotNull
    private Pauta pauta;

}
