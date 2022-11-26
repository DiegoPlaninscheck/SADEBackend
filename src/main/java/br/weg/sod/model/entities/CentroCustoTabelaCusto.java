package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "centroCustoTabelaCusto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CentroCustoTabelaCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer idCentroCustoDemanda;

    @Column(length = 3, nullable = false)
    private Double porcentagemDespesa;

    @ManyToOne
    @JoinColumn(name = "idTabelaCusto", nullable = false)
    private TabelaCusto tabelaCusto;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private CentroCusto centroCusto;

}
