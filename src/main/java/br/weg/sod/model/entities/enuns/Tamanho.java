package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tamanho {

    MUITOPEQUENO("Muito Pequeno", "1 a 40h"),
    PEQUENO("Pequeno", "41 a 300h"),
    MEDIO("Médio", "301 a 1000h"),
    GRANDE("Grande", "1001 a 3000h"),
    MUITOGRANDE("Muito Grande", "+3000h");

    private String nome;
    private String tamanho;
}
