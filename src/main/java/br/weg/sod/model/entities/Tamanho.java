package br.weg.sod.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tamanho {

    PEQUENO(""),
    MEDIO(""),
    GRANDE(""),
    MUITOGRANDE("");

    private String nome;

}
