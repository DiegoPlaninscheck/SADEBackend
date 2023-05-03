package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Frequencia {

    DIARIAMENTE("Diariamente"),
    SEMANALMENTE("Semanalmente"),
    MENSALMENTE("Mensalmente");

    private String nome;
}
