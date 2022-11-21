package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Usuario {

    @Id
    @Column
    private Integer idUsuario;

    @Column(nullable = false)
    private Integer numeroCadastro;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String departamento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    @Lob
    private byte[] foto;

    @Column(nullable = false)
    private String setor;

    @Column(nullable = false)
    private String cargo;

//    @OneToMany
//    @JoinColumn(name = "idUsuario", nullable = false)
//    private Integer responsaveisNegocioIdUsuario;

    @ManyToMany
    @JoinTable(name = "notificacoesUsuario", joinColumns = @JoinColumn(name = "idUsuario", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idNotificacao", nullable = false))
    private List<Notificacao> notificacoesUsuario;

//    @OneToMany
//    @JoinColumn(name = "idUsuario", nullable = false)
//    private Integer forumIdUsuario;

}
