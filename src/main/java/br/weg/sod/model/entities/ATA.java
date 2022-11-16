package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ATA")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ATA {

    @Id
    @Column
    private Integer idATA;

    @Column(nullable = false)
    private byte[] pdf;

    @Column
    private Integer numeroDG;

    @Column
    private byte[] documentoAprovacao;

}
