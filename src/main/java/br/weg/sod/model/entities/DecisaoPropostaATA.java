package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "decisaoPropostaATA")
public class DecisaoPropostaATA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idDecisaoPropostaAta;

    @Column(nullable = false, unique = true)
    private Long numeroSequencial;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusDemanda statusDemandaComissao;

    @Column
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta proposta;
}
