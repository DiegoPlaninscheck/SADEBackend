package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Moeda {

    REAL("Real"),
    DOLAR("Dolar"),
    EURO("Euro");

    private String nome;

}
