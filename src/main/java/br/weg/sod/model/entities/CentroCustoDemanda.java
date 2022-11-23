package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "centroCustoDemanda")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CentroCustoDemanda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer idCentroCustoDemanda;

    @Column(length = 3)
    private Double porcentagemDespesa;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda demanda;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private CentroCusto centroCusto;

}
