package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoDocumento {

    ATAREUNIAO("ATA da reunião"),
    ATAPUBLICADA("ATA publicada"),
    ATANAOPUBLICADA("ATA não publicada"),
    DOCUMENTOAPROVACAO("Documento de aprovação");

    private String nome;
}
