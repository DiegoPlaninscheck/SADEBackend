package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @Column
    private Integer idCentroCusto;

    @Column(nullable = false)
    private String nome;
}
