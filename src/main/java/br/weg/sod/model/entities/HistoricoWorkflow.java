package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "historicoWorkflow")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HistoricoWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer idHistoricoWorkflow;

    @Column
    private Timestamp recebimento;

    @Column
    private Timestamp prazo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tarefa tarefa;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusHistorico status;

    @Column
    @Lob
    private byte[] pdfHistorico;

    @Column
    private String motivoDevolucao;

    @Column
    private Timestamp conclusaoTarefa;

    @Column
    @Enumerated(EnumType.STRING)
    private Tarefa acaoFeita;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda demanda;


    //ainda checar pdf_historico para as criações
    /**
     * Construtor para início de workflow
     *
     * @param tarefa
     * @param status
     * @param demanda
     */
    public HistoricoWorkflow(Tarefa tarefa, StatusHistorico status, Demanda demanda) {
        this.tarefa = tarefa;
        this.status = status;
        this.demanda = demanda;
    }

    /**
     * Construtor para histórico no meio do workflow
     *
     * @param recebimento
     * @param prazo
     * @param tarefa
     * @param status
     * @param usuario
     * @param demanda
     */
    public HistoricoWorkflow(Timestamp recebimento, Timestamp prazo, Tarefa tarefa, StatusHistorico status, Usuario usuario, Demanda demanda) {
        this.recebimento = recebimento;
        this.prazo = prazo;
        this.tarefa = tarefa;
        this.status = status;
        this.usuario = usuario;
        this.demanda = demanda;
    }

}
