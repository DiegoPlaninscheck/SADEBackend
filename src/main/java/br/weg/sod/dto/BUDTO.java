package br.weg.sod.dto;

import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Getter
public class BUDTO {

    @Digits(integer = 10, fraction = 0)
    private Integer idBU;

    @NotBlank
    private String nomeBU;

}
