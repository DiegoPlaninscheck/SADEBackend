package br.weg.sod.model.entities;

import javax.persistence.*;

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
    @JoinColumn(name = "usuarioIdUsuario", nullable = false)
    private Integer usuariosForumIdUsuario;

    @OneToMany
    @JoinColumn(name = "usuarioIdUsuario", nullable = false)
    private Integer responsavelNegocioIdUsuario;

    @OneToMany
    @JoinColumn(name = "usuarioIdUsuario", nullable = false)
    private Integer notificacoesUsuarioIdUsuario;

}
