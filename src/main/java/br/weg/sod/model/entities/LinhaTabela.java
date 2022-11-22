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
    @Column
    private Integer idLinhaTabela;

    @Column(nullable = false)
    private String recurso;

    @Column(nullable = false)
    private Integer valor;

    @Column(nullable = false)
    private Double valorHora;

    @ManyToOne
    @JoinColumn(name = "idTabelaCusto", nullable = false)
    private TabelaCusto tabelaCusto;

}
