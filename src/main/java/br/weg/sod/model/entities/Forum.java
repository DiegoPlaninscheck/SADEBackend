package br.weg.sod.model.entities;

import javax.persistence.*;

public class Forum {

    @Id
    @Column
    private Integer idForum;

    @Column(nullable = false)
    private String nomeForum;

    @OneToOne
    private Integer analistaIdUsuario;


    @OneToMany
    @JoinColumn(name = "idForum", nullable = false)
    private Integer forumIdForum;

}
