package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.TipoNotificacao;
import lombok.*;

import javax.persistence.*;

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

}
