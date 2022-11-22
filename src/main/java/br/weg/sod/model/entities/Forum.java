package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.TipoForum;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer idForum;

    @Column(nullable = false)
    private String nomeForum;

    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario analistaResponsavel;

    @ManyToMany
    @JoinTable(name = "usuariosForum", joinColumns = @JoinColumn(name = "idForum", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idUsuario", nullable = false))
    private List<Usuario> usuariosForum;

}
