package br.weg.sod.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusComissao {

    APROVADO("Aprovado"),
    REPROVADO("Reprovado");

    private String nome;
}
