package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

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

    @OneToMany
    @JoinColumn(name = "idUsuario", nullable = false)
    private Integer responsaveisNegocioIdUsuario;

    @OneToMany
    @JoinColumn(name = "idUsuario", nullable = false)
    private Integer notificacoesUsuarioIdUsuario;

    @OneToMany
    @JoinColumn(name = "idUsuario", nullable = false)
    private Integer usuariosForumIdUsuario;

}
