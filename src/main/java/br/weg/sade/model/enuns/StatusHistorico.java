package br.weg.sade.model.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusHistorico {

    EMAGUARDO("Em Aguardo"),
    EMANDAMENTO("Em Andamento"),
    CONCLUIDO("Concluído"),
    ATRASADO("Atrasado"),
    CONCLUIDOCOMATRASO("Concluído com Atraso");

    String nome;

}
