package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.SecaoTI;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.Tamanho;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "demanda")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Demanda {

    @Id
    @Column
    private Integer idDemanda;

    @Column
    private String titulo;

    @Column(nullable = false)
    private StatusDemanda statusDemanda;

    @Column
    private Tamanho tamanho;

    @Column
    private String objetivo;

    @Column
    private SecaoTI secaoTIResponsavel;

    @Column
    private String situacaoAtual;

    @Column
    private Integer frequenciaUso;

    @Column
    private Time prazoElaboracao;

    @Column
    private Integer codigoPPM;

    @Column
    private String linkJira;

    @Column
    private Double score;

    @ManyToOne
    @JoinColumn(name = "idBU")
    private BU BUSolicitante;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "busBeneficiadas", joinColumns = @JoinColumn(name = "idDemanda", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idBU", nullable = false))
    private List<BU> busBeneficiadas;

}
