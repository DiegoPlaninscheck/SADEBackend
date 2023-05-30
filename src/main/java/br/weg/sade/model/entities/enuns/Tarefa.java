package br.weg.sade.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tarefa {

    CRIARDEMANDA("Criar Demanda"),
    AVALIARDEMANDA("Avaliar Demanda"),
    APROVARDEMANDA("Aprovar Demanda"),
    REPROVARDEMANDA("Reprovar Demanda"),
    DEVOLVERDEMANDA("Devolver Demanda"),
    REENVIARDEMANDA("Reenviar Demanda"),
    CLASSIFICARDEMANDA("Classificar Demanda"),
    ADICIONARINFORMACOESDEMANDA("Adicionar Informações Demanda"),
    CRIARPROPOSTA("Criar Proposta"),
    CRIARPAUTA("Criar Pauta"),
    INICIARWORKFLOW("Iniciar Workflow"),
    AVALIARWORKFLOW("Avaliar Workflow"),
    APROVARWORKFLOW("Aprovar Workflow"),
    REPROVARWORKFLOW("Reprovar Workflow"),
    INFORMARPARECERFORUM("Informar Parecer Fórum"),
    CRIARATA("Criar ata"),
    INFORMARPARECERDG("Informar Parecer DG"),
    FINALIZAR("Finalizar histórico da demanda");

    String nome;
}
