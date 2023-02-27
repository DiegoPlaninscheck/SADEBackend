package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.SecaoTI;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.Tamanho;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "demanda")
public class Demanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idDemanda;

    @Column
    private String tituloDemanda;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusDemanda statusDemanda;

    @Column
    @Enumerated(EnumType.STRING)
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
    private Date prazoElaboracao;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idDemanda", nullable = false)
    private List<ArquivoDemanda> arquivosDemanda = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idDemanda", nullable = false)
    private List<Beneficio> beneficiosDemanda;

    @ManyToMany
    @JoinTable(name = "budemanda", joinColumns =
    @JoinColumn(name = "idDemanda", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idBU", nullable = false))
    private List<BU> BUsBeneficiadas;

    @ManyToMany
    @JoinTable(name = "centroCustoDemanda", joinColumns = @JoinColumn(name = "idDemanda", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "id_centrocusto", nullable = false))
    private List<CentroCusto> centroCustoDemanda;

}
