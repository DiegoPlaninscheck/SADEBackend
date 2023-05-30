package br.weg.sade.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "forum")
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idForum;

    @Column(nullable = false)
    private String nomeForum;

    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private AnalistaTI analistaResponsavel;

    @ManyToMany
    @JoinTable(name = "usuariosForum", joinColumns = @JoinColumn(name = "idForum", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idUsuario", nullable = false))
    private List<Usuario> usuariosForum;

}
