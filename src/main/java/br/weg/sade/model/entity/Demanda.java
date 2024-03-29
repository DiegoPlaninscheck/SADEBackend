package br.weg.sade.model.entity;

import br.weg.sade.model.enums.Frequencia;
import br.weg.sade.model.enums.SecaoTI;
import br.weg.sade.model.enums.StatusDemanda;
import br.weg.sade.model.enums.Tamanho;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

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

    @Column
    @Enumerated(EnumType.STRING)
    private StatusDemanda statusDemanda;

    @Column
    @Enumerated(EnumType.STRING)
    private Tamanho tamanho;

    @Column(length = 2000)
    private String objetivo;

    @Enumerated(EnumType.STRING)
    @Column
    private SecaoTI secaoTIResponsavel;

    @Column(length = 2000)
    private String situacaoAtual;

    @Column
    @Enumerated(EnumType.STRING)
    private Frequencia frequenciaUso;

    @Column
    private Date prazoElaboracao;

    @Column
    private Integer codigoPPM;

    @Column
    private String linkJira;

    @Column
    private Double score;

    @Column
    private boolean rascunho = false;

    @Column
    private boolean pertenceUmaProposta = false;

    @Column
    private boolean temChat = false;

    @Column
    private boolean devolvida = false;

    @ManyToOne
    @JoinColumn(name = "idBU")
    private BU BUSolicitante;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
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


    @JsonIgnore
    @OneToMany(mappedBy = "demanda", cascade = CascadeType.ALL)
    private List<HistoricoWorkflow> historicosWorkflowDemanda;


    @Override
    public String toString() {
        return "Demanda{" +
                "idDemanda=" + idDemanda +
                ", tituloDemanda='" + tituloDemanda + '\'' +
                ", statusDemanda=" + statusDemanda +
                ", tamanho=" + tamanho +
                ", objetivo='" + objetivo + '\'' +
                ", secaoTIResponsavel=" + secaoTIResponsavel +
                ", situacaoAtual='" + situacaoAtual + '\'' +
                ", frequenciaUso=" + frequenciaUso +
                ", prazoElaboracao=" + prazoElaboracao +
                ", codigoPPM=" + codigoPPM +
                ", linkJira='" + linkJira + '\'' +
                ", score=" + score +
                ", rascunho=" + rascunho +
                ", pertenceUmaProposta=" + pertenceUmaProposta +
                ", BUSolicitante=" + BUSolicitante +
                ", usuario=" + usuario +
                ", BUsBeneficiadas=" + BUsBeneficiadas +
                ", centroCustoDemanda=" + centroCustoDemanda +
                '}';
    }
}
