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
    @Column
    private Integer idNotificacao;

    @Column(nullable = false)
    private String notificacao;

    @Column(nullable = false)
    private String link;

//    @OneToMany
//    @JoinColumn(name = "idNotificacao", nullable = false)
//    private Integer notificacaoIdNotificacao;
}
