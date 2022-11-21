package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
    private Integer payback;

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
    private Demanda idDemanda;

    @ManyToMany
    @JoinTable(name = "responsaveisNegocio", joinColumns = @JoinColumn(name = "idProposta", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idUsuario", nullable = false))
    private List<Usuario> responsaveisNegocio;

}
