package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AcaoNotificacao {
    DEMANDAAPROVADA("Demanda aprovada"),
    ADICAOINFORMACOESDEMANDA("Adição de informações a demanda"),
    AVALIARDEMANDA("Avaliar demanda"),
    REDEFINICAOREQUERIDA("Redefinição Requerida"),
    NOVOWORKFLOWAPROVACAO("Novo Workflow aprovação"),
    CHAT("Chat"),
    REUNIAO("Reunião"),
    STATUSDEMANDA("Status Demanda"),
    PRAZOELABORACAO("Prazo de elaboração"),
    VIROUPROPOSTA("Virou proposta"),
    VIROUPAUTA("Virou pauta"),
    VIROUATA("Virou ATA"),
    AVALIACAODG("Avaliação Direção Geral");

    private String nome;
}
