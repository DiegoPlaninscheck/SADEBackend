package br.weg.sod.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Moeda {

    BRL("Real"),
    DOLAR("Dolar"),
    EURO("Euro");

    private String nome;

}
