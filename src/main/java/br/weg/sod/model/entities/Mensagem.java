package br.weg.sod.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

public class Mensagem {

    @Id
    @Column
    private Integer idMensagem;

    @Column(nullable = false)
    private String mensagem;

    @Column
    private byte[] arquivo;

    @Column(nullable = false)
    private Timestamp dataHoraMensagem;

    @ManyToOne
    @JoinColumn(name = "idChat", nullable = false)
    private Integer idChat;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Integer idUsuario;

}
