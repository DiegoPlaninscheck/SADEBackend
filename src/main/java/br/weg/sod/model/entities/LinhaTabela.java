package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "linhaTabela")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LinhaTabela {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer idLinhaTabela;

    @Column(nullable = false)
    private String nomeRecurso;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Double valorQuantidade;

    @ManyToOne
    @JoinColumn(name = "idTabelaCusto", nullable = false)
    private TabelaCusto tabelaCusto;

}
