package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "mensagem")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
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
