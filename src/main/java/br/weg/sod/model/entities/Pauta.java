package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
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

    @Column
    @Lob
//    @JsonIgnore
    private byte[] ataReuniao;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPauta", nullable = false)
    private List<DecisaoPropostaPauta> propostasPauta;

    @ManyToOne
    @JoinColumn(name = "idForum", nullable = false)
    private Forum forum;

}
