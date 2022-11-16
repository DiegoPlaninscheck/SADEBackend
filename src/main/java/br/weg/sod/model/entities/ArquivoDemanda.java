package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class ArquivoDemanda {

    @Id
    @Column
    private Integer idArquivoDemanda;

    @Column(nullable = false)
    private byte[] arquivo;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Integer idDemanda;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Integer idUsuario;
}
