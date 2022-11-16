package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

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
    private String nomeForum;

    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Integer analistaIdUsuario;

    @OneToMany
    @JoinColumn(name = "idForum", nullable = false)
    private Integer forumIdForum;

}
