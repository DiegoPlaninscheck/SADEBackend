package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "forum")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Forum {

    @Id
    @Column
    private Integer idForum;

    @Column(nullable = false)
    private Forum nomeForum;

    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario analistaResponsavel;

    @ManyToMany
    @JoinTable(name = "usuariosForum", joinColumns = @JoinColumn(name = "idForum", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idUsuario", nullable = false))
    private List<Usuario> usuariosForum;

}
