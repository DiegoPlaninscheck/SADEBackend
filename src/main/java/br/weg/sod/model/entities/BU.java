package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "BU")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bu")
    private Integer idBU;

    @Column(nullable = false)
    private String nomeBU;

//    @ManyToMany(mappedBy = "BUsBeneficiadas")
////    @JoinTable(name = "busBeneficiadas", joinColumns = @JoinColumn(name = "idDemanda", nullable = false),
////            inverseJoinColumns = @JoinColumn(name = "id_bu", nullable = false))
//    private List<Demanda> demandas;

}
