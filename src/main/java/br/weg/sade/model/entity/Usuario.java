package br.weg.sade.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
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

    @JsonIgnore
    @Column(nullable = false)
    private String senha = "123";

    @Column
    @Lob
    @JsonIgnore
    private byte[] foto;

    @Column(nullable = false)
    private String setor;

    @Column(nullable = false)
    private String cargo;

    @ManyToMany(mappedBy = "usuariosNotificacao",cascade = CascadeType.ALL)
    private List<Notificacao> notificacoesUsuario = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "usuariosChat")
    private List<Chat> chatsUsuario;


    public void setSenha(String senha){
        this.senha = new BCryptPasswordEncoder().encode(senha);
    }

    @Override
    public String toString() {
        return "{'idUsuario':" + idUsuario + ",'numeroCadastro':" + numeroCadastro + ",'nomeUsuario': '" + nomeUsuario + "'" + ",'departamento': '" + departamento + "'" + ",'email':'" + email + "'" + ",'senha':'" + senha + "'" + ",'setor':'" + setor + "'" + ",'cargo':'" + cargo + "'}";
    }
}
