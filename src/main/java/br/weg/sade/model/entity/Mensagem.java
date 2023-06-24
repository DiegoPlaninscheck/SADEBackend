package br.weg.sade.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "mensagem")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idMensagem;

    @Column(nullable = false, length = 2000)
    private String mensagem;

    @Column
    @Lob
    private byte[] arquivo;

    @Column(nullable = false)
    private Timestamp dataHoraMensagem;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idChat", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;
}
