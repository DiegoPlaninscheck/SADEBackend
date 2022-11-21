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
    @Column
    private Integer idTabelaCusto;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer valor;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private Boolean licenca;

    @ManyToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta idProposta;
}
