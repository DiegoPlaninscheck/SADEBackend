package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "decisaoPropostaPauta")
public class DecisaoPropostaPauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idDecisaoPropostaPauta;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusDemanda statusDemandaComissao;

    @Column
    private Boolean ataPublicada;

    @Column
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta proposta;

    public DecisaoPropostaPauta(StatusDemanda statusDemandaComissao, Boolean ataPublicada, String comentario, Proposta proposta) {
        this.statusDemandaComissao = statusDemandaComissao;
        this.ataPublicada = ataPublicada;
        this.comentario = comentario;
        this.proposta = proposta;
    }
}
