package br.weg.sod.model.entities;

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
    private Status status;

    @Column
    private Tamanho tamanho;

    @Column
    private String objetivo;

    @Column
    private String secaoTIResponsavel;

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
    private BU idBUSolicitante;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;

    @ManyToMany
    @JoinTable(name = "busBeneficiadas", joinColumns = @JoinColumn(name = "idDemanda", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idBU", nullable = false))
    private List<BU> busBeneficiadas;

}
