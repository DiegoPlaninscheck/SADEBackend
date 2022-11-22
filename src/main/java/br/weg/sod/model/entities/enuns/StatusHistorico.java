package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusHistorico {

    EMAGUARDO("Em Aguardo"),
    EMANDAMENTO("Em Andamento"),
    CONCLUIDO("Concluído"),
    ATRASADO("Atrasado");

    String nome;

}
