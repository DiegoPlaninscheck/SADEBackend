package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AcaoNotificacao {
    DEMANDAAPROVADA("Demanda aprovada"),
    REDEFINICAOREQUERIDA("Redefinição Requerida"),
    NOVOWORKFLOWAPROVACAO("Novo Workflow aprovação"),
    CHAT("Chat"),
    REUNIAO("Reunião"),
    STATUSDEMANDA("Status Demanda"),
    PRAZOELABORACAO("Prazo de elaboração");

    private String nome;
}
