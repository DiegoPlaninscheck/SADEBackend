package br.weg.sade.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class BUDTO {

    @NotBlank
    private String nomeBU;

}
