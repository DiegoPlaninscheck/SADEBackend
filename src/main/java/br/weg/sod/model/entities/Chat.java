package br.weg.sod.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @Column
    private Integer idChat;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda demanda;

    @OneToMany(mappedBy = "chat")
    private List<Mensagem> mensagens;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "usuariosChat")
    private List<Usuario> usuariosChat;

}
