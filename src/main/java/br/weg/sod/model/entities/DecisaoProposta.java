package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "decisaoProposta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DecisaoProposta {

    @Column
    private StatusComissao statusComissao;

    @Column
    private Boolean ataPublicada;

    @Column
    private String comentario;

    @Column
    private Integer numeroSequencial;

    @ManyToOne
    @JoinColumn(name =  "idProposta", nullable = false)
    private Integer idProposta;

    @ManyToOne
    @JoinColumn(name =  "idPauta", nullable = false)
    private Integer idPauta;

    @ManyToOne
    @JoinColumn(name =  "idATA")
    private Integer idATA;


}
