package br.weg.sod.dto;


import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
public class ATADTO {

    @Digits(integer = 10, fraction = 0)
    private Integer idATA;

    @NotNull
    private byte[] pdf;

    @Digits(integer = 100, fraction = 0)
    private Integer numeroDG;

    @NotNull
    private byte[] documentoAprovacao;

}
