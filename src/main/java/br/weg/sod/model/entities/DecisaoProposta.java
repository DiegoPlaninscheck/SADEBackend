package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
