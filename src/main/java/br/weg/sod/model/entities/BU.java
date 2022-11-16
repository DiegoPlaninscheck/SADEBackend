package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

public class BU {

    @Id
    @Column
    private Integer idBU;

    @Column(nullable = false)
    private String nomeBU;

    @OneToMany
    @JoinColumn(name = "busBeneficiadasIdBu", nullable = false)
    private Integer buidBU;

}
