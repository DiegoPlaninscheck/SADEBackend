package br.weg.sod.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tabelaCusto")
public class TabelaCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idTabelaCusto;

    @Column(nullable = false)
    private String tituloTabela;

    @Column(nullable = false)
    private Integer quantidadeTotal;

    @Column(nullable = false)
    private Double valorTotal;

    @Column(nullable = false)
    private Boolean licenca;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idTabelaCusto", nullable = false)
    private List<CentroCustoPagante> centrosCustoPagantes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idTabelaCusto", nullable = false)
    private List<LinhaTabela> linhasTabela;
}
