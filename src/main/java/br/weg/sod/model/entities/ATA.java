package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer idATA;

    @Column(nullable = false)
    @Lob
    private byte[] pdfATAPublicada;

    @Column(nullable = false)
    @Lob
    private byte[] pdfATANaoPublicada;

    @Column
    private Integer numeroDG;

    @Column
    @Lob
    private byte[] documentoAprovacao;

}
