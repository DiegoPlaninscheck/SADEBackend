package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
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

    @Override
    public String toString() {
        return "DecisaoPropostaPauta{" +
                "idDecisaoPropostaPauta=" + idDecisaoPropostaPauta +
                ", statusDemandaComissao=" + statusDemandaComissao +
                ", ataPublicada=" + ataPublicada +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}
