package br.weg.sade.model.entities;

import br.weg.sade.model.entities.enuns.Moeda;
import br.weg.sade.model.entities.enuns.TipoBeneficio;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "beneficio")
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idBeneficio;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoBeneficio tipoBeneficio;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @Column
    @Enumerated(EnumType.STRING)
    private Moeda moeda;

    @Column
    private Double valor;

}
