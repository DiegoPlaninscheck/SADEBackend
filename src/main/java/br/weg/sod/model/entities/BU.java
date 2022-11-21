package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "BU")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BU {

    @Id
    @Column
    private Integer idBU;

    @Column(nullable = false)
    private String nomeBU;

}
