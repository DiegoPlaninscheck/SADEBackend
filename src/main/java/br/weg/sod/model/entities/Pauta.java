package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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
    @Column
    private Integer idPauta;

    @Column(nullable = false)
    private Date dataReuniao;

    @OneToOne
    @JoinColumn(name = "idForum", nullable = false)
    private Forum idForum;

}
