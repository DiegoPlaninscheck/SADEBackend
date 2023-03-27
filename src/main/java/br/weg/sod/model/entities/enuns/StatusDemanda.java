package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusDemanda {

    BACKLOG("Backlog"),
    ASSESMENT("Assessment"),
    BUSINESSCASE("Business Case"),
    CANCELED("Canceled"),
    TODO("To Do");

    private String nome;
    
}
