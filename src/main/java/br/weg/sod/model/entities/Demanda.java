package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Time;

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
    private String tituloDemanda;

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

    @OneToOne
    @JoinColumn(name = "idBU")
    private Integer idBUSolicitante;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Integer idUsuario;

    @OneToMany
    @JoinColumn(name = "idDemanda", nullable = false)
    private Integer demandaIdDemanda;

}
