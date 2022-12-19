package br.weg.sod.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "pauta")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idPauta;

    @Column(nullable = false)
    private Date dataReuniao;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPauta", nullable = false)
    private List<ArquivoPauta> arquivosPauta = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPauta", nullable = false)
    private List<DecisaoPropostaPauta> propostasPauta;

    @ManyToOne
    @JoinColumn(name = "idForum", nullable = false)
    private Forum forum;

}
