package br.weg.sade.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "BU")
public class BU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bu")
    private Integer idBU;

    @Column(nullable = false)
    private String nomeBU;

}
