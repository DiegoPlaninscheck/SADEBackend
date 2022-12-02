package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tabelaCusto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TabelaCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idTabelaCusto;

    @Column(nullable = false)
    private String tituloTabela;

    @Column(nullable = false)
    private Integer quantidadeTotal;

    @Column(nullable = false)
    private Double valorTotal;

    @Column(nullable = false)
    private Boolean licenca;

    @ManyToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta proposta;
}
