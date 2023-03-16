package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "proposta")
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
    private Boolean estaEmPauta = false;

    @Column(nullable = false)
    private Boolean avaliadoWorkflow = false;

    @Column(nullable = false)
    private Boolean aprovadoWorkflow = false;

    @Column(nullable = false)
    private Boolean emWorkflow = false;

    @OneToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda demanda;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idProposta", nullable = false)
    private List<TabelaCusto> tabelasCustoProposta = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "responsaveisNegocio", joinColumns = @JoinColumn(name = "idProposta", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idUsuario", nullable = false))
    private List<Usuario> responsaveisNegocio = new ArrayList<>();

}
