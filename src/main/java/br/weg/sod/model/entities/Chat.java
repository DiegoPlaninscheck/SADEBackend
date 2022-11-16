package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class Chat {

    @Id
    @Column
    private Integer idChat;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToOne
    private Integer idDemanda;

}
