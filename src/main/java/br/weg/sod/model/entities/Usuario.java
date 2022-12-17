package br.weg.sod.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idUsuario;

    @Column(nullable = false)
    private Integer numeroCadastro;

    @Column(nullable = false)
    private String nomeUsuario;

    @Column(nullable = false)
    private String departamento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column
    @Lob
    @JsonIgnore
    private byte[] foto;

    @Column(nullable = false)
    private String setor;

    @Column(nullable = false)
    private String cargo;

    @ManyToMany
    @JoinTable(name = "notificacoesUsuario", joinColumns = @JoinColumn(name = "idUsuario", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idNotificacao", nullable = false))
    private List<Notificacao> notificacoesUsuario;

    @ManyToMany
    @JoinTable(name = "chatsUsuario", joinColumns = @JoinColumn(name = "idUsuario", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idChat", nullable = false))
    private List<Chat> chatsUsuario;
}
