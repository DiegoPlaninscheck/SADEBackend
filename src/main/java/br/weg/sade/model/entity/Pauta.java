package br.weg.sade.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
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
    private String tituloReuniaoPauta;

    @Column(nullable = false)
    private Date dataReuniao;

    @Column(nullable = false)
    private Time inicioReuniao;

    @Column(nullable = false)
    private Time finalReuniao;

    @Column
    private boolean pertenceUmaATA = false;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPauta", nullable = false)
    private List<ArquivoPauta> arquivosPauta = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPauta", nullable = false)
    private List<DecisaoPropostaPauta> propostasPauta = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "idForum", nullable = false)
    private Forum forum;

    @Override
    public String toString() {
        return "Pauta{" +
                "idPauta=" + idPauta +
                ", dataReuniao=" + dataReuniao +
                '}';
    }
}
