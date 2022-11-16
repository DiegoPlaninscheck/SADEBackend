package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

public class Notificacao {

    @Id
    @Column
    private Integer idNotificacao;

    @Column(nullable = false)
    private String notificacao;

    @Column(nullable = false)
    private String link;

    @OneToMany
    @JoinColumn(name = "notificacoesUsuarioIdNotificacao", nullable = false)
    private Integer notificacoesUsuarioIdNotificacao;
}
