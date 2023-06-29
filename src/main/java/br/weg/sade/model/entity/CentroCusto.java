package br.weg.sade.model.entity;


import lombok.Data;

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

    @Column(nullable = false)
    private Long numeroCentroCusto;

}
