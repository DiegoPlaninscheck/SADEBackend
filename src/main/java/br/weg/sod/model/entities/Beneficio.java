package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "beneficio")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Beneficio {

    @Id
    @Column
    private Integer idBeneficio;

    @Column(nullable = false)
    private TipoBeneficio tipoBeneficio;

    @Column(nullable = false)
    private String descricao;

    @Column
    private Moeda moeda;

    @Column
    private Double memoriaCalculo;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Integer idDemanda;
}
