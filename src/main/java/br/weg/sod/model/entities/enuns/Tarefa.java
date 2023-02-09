package br.weg.sod.model.entities.enuns;

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
    ADICIONARINFORMACOES("Adicionar Informações"),
    CRIARPROPOSTA("Criar Proposta"),
    INICIARWORKFLOW("Iniciar Workflow"),
    AVALIARWORKFLOW("Avaliar Workflow"),
    CRIARPAUTA("Criar Pauta"),
    INFORMARPARECERFORUM("Informar Parecer Fórum"),
    INFORMARPARECERDG("Informar Parecer DG"),
    FINALIZAR("Finalizar histórico da demanda");

    String nome;
}
