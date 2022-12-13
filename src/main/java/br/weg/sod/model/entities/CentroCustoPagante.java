package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "centroCustoPagante")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CentroCustoPagante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idCentroCustoPagante;

    @Column(length = 3, nullable = false)
    private Double porcentagemDespesa;

    @ManyToOne
    @JoinColumn(name = "idTabelaCusto", nullable = false)
    private TabelaCusto tabelaCusto;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private CentroCusto centroCusto;

}
