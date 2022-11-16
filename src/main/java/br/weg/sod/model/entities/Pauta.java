package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

public class Pauta {

    @Id
    @Column
    private Integer idPauta;

    @Column
    private Date dataReuniao;

    @OneToOne
    private Integer idForum;

}
