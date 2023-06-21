package br.weg.sade.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusDemanda {

    BACKLOG("Backlog"),
    ASSESSMENT("Assessment"),
    BUSINESSCASE("Business Case"),
    CANCELLED("Cancelled"),
    TODO("To Do");

    private String nome;

}
