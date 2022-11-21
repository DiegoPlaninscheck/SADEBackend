package br.weg.sod.model.entities.enuns;

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
