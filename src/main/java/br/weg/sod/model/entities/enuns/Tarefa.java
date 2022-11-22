package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tarefa {

    AVALIARDEMANDA("Avaliar Demanda"),
    CLASSIFICAR("Classificar Demanda"),
    APROVAR("Aprovar"),
    REPROVAR("Reprovar"),
    DEVOLVER("Devolver"),
    REENVIAR("Reenviar"),
    ADICIONARINFORMACOES("Adicionar Informações"),
    CRIARPROPOSTA("Criar Proposta"),
    INICIARWORKFLOW("Iniciar Workflow"),
    AVALIARWORKFLOW("Avaliar Workflow"),
    CRIARPAUTA("Criar Pauta"),
    INFORMARPARECERFORUM("Informar Parecer Fórum"),
    INFORMARPARECERDG("Informar Parecer DG");


    String nome;
}
