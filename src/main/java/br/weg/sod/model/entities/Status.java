package br.weg.sod.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {

    BACKLOG("Backlog"),
    ASSESSMENT("Assessment"),
    DESIGNANDBUILD("Design and Build"),
    CANCELED("Canceled"),
    TODO("To do");

    private String nome;


}
