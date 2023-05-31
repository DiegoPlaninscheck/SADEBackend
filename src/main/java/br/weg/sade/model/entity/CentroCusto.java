package br.weg.sade.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "centroCusto")
public class CentroCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_centrocusto")
    private Integer idCentroCusto;

    @Column(nullable = false)
    private String nomeCentroCusto;

}
