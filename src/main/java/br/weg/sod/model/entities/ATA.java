package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;

public class ATA {

    @Id
    @Column
    private Integer idATA;

    @Column(nullable = false)
    private byte[] pdf;

    @Column
    private Integer numeroDG;

    @Column
    private byte[] documentoAprovacao;

}
