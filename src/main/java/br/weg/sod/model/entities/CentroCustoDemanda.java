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

    @Column
    private Double porcentagemDespesa;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Integer idDemanda;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private Integer idCentroCusto;

}
