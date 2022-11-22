package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "notificacao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer idNotificacao;

    @Column(nullable = false)
    private String tituloNotificacao;

    @Column(nullable = false)
    private String descricaoNotificacao;

    @Column(nullable = false)
    private String linkNotificacao;

}
