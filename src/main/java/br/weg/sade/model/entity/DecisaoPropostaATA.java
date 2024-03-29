package br.weg.sade.model.entity;

import br.weg.sade.model.enums.StatusDemanda;
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

    @Column( unique = true)
    private Long numeroSequencial;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusDemanda statusDemandaComissao;

    @Column(length = 2000)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta proposta;

    @Override
    public String toString() {
        return "DecisaoPropostaATA{" +
                "idDecisaoPropostaAta=" + idDecisaoPropostaAta +
                ", numeroSequencial=" + numeroSequencial +
                ", statusDemandaComissao=" + statusDemandaComissao +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}
