package br.weg.sade.model.entity;

import br.weg.sade.model.enums.Moeda;
import br.weg.sade.model.enums.TipoBeneficio;
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

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column
    @Enumerated(EnumType.STRING)
    private Moeda moeda;

    @Column
    private Double valor;

}
