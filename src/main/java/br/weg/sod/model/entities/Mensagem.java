package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "mensagem")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idMensagem;

    @Column(nullable = false)
    private String mensagem;

    @Column
    @Lob
    private byte[] arquivo;

    @Column(nullable = false)
    private Timestamp dataHoraMensagem;

    @ManyToOne
    @JoinColumn(name = "idChat", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

}
