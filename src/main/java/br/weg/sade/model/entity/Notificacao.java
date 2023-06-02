package br.weg.sade.model.entity;

import br.weg.sade.model.enums.AcaoNotificacao;
import br.weg.sade.model.enums.TipoNotificacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "notificacao")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idNotificacao;

    @Column(nullable = false)
    private String tituloNotificacao;

    @Column(nullable = false)
    private String descricaoNotificacao;

    @Column(nullable = false)
    private String linkNotificacao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoNotificacao tipoNotificacao;

    @Column(nullable = false)
    private Integer idComponenteLink;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AcaoNotificacao acao;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "usuariosNotificacao", joinColumns = @JoinColumn(name = "idNotificacao", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idUsuario", nullable = false))
    private List<Usuario> usuariosNotificacao;

}
