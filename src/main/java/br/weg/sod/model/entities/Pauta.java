package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pauta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idPauta;

    @Column(nullable = false)
    private Date dataReuniao;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPauta", nullable = false)
    private List<DecisaoProposta> propostasPauta;

    @ManyToOne
    @JoinColumn(name = "idForum", nullable = false)
    private Forum forum;

}
