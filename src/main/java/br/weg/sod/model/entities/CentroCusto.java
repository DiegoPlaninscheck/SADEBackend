package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "centroCusto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CentroCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer idCentroCusto;

    @Column(nullable = false)
    private String nomeCentroCusto;
}
