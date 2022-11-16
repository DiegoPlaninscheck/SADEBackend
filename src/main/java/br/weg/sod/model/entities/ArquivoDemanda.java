package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "arquivoDemanda")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ArquivoDemanda {

    @Id
    @Column
    private Integer idArquivoDemanda;

    @Column(nullable = false)
    private byte[] arquivo;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Integer idDemanda;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Integer idUsuario;
}
