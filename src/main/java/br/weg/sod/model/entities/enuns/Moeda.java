package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Moeda {

    DOLAR("Dolar"),
    EURO("Euro"),
    REAL("Real");

    private String nome;

}
