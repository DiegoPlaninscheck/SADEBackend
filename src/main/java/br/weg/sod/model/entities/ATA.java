package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
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
    private Integer numeroDG;

    @OneToOne
    @JoinColumn(name = "idPauta", nullable = false)
    private Pauta pauta;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idAta", nullable = false)
    private List<DecisaoPropostaATA> propostasAta;

}
