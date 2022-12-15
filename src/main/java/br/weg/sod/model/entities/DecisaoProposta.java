package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "decisaoProposta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DecisaoProposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idDecisaoProposta;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusDemanda statusDemandaComissao;

    @Column
    private Boolean ataPublicada;

    @Column
    private String comentario;

    @Column
    private Integer numeroSequencial;

    @Column
    private Integer numeroAno;

    @ManyToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta proposta;

    @ManyToOne
    @JoinColumn(name = "idATA")
    private ATA ATA;

}
