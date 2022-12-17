package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @Column
    private Integer idChat;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda demanda;

}
