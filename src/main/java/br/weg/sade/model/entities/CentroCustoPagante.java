package br.weg.sade.model.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "centroCustoPagante")
public class CentroCustoPagante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idCentroCustoPagante;

    @Column(length = 3, nullable = false)
    private Double porcentagemDespesa;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private CentroCusto centroCusto;

}
