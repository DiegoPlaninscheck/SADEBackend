package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "proposta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Proposta {

    @Id
    @Column
    private Integer idProposta;

    @Column(nullable = false)
    private String escopo;

    @Column(nullable = false)
    private Double payback;

    @Column(nullable = false)
    private Date periodoExecucaoInicio;

    @Column(nullable = false)
    private Date periodoExecucaoFim;

    @Column(nullable = false)
    private Boolean aprovadoWorkflow;

    @Column(nullable = false)
    private Boolean emWorkflow;

    @OneToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Integer idDemanda;

    @OneToMany
    @JoinColumn(name = "idProposta", nullable = false)
    private Integer propostaIdProposta;

}
