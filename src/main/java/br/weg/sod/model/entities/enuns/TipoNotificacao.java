package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoNotificacao {

    DEMANDA("Demanda"),
    PROPOSTA("Proposta"),
    PAUTA("Pauta"),

    ATA("ATA"),
    CHAT("Chat");

    String nomeTipo;
}
