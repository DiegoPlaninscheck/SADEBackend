package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.Moeda;
import br.weg.sod.model.entities.enuns.TipoBeneficio;
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
    @Enumerated(EnumType.STRING)
    private TipoBeneficio tipoBeneficio;

    @Column(nullable = false)
    private String descricao;

    @Column
    @Enumerated(EnumType.STRING)
    private Moeda moeda;

    @Column
    private String memoriaCalculo;

    @Column
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda demanda;
}
