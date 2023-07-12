package br.weg.sade.model.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "ATA")
public class ATA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idATA;

    @Column
    private Integer numeroAno;

    @Column
    private Long numeroDG;

    @Column(nullable = false)
    private String tituloReuniaoATA;

    @Column(nullable = false)
    private Date dataReuniao;

    @Column(nullable = false)
    private Time inicioReuniao;

    @Column(nullable = false)
    private Time finalReuniao;

    @OneToOne
    @JoinColumn(name = "idPauta", nullable = false)
    private Pauta pauta;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "idAta", nullable = false)
    private List<DecisaoPropostaATA> propostasAta;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "usuariosReuniaoATA", joinColumns = @JoinColumn(name = "idATA"),
            inverseJoinColumns = @JoinColumn(name = "idUsuario"))
    private List<Usuario> usuariosReuniaoATA;

}
