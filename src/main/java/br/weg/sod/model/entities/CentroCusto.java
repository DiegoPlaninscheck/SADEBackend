package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "centroCusto")
public class CentroCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_centrocusto")
    private Integer idCentroCusto;

    @Column(nullable = false)
    private String nomeCentroCusto;

}
