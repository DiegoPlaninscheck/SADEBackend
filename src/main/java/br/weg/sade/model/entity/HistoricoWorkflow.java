package br.weg.sade.model.entity;

import br.weg.sade.model.enuns.StatusHistorico;
import br.weg.sade.model.enuns.Tarefa;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@Table(name = "historicoWorkflow")
public class HistoricoWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idArquivoHistorico")
    private ArquivoHistoricoWorkflow arquivoHistoricoWorkflow;

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

    /**
     * Primeira instância de um histórico da demanda
     *
     * @param status
     * @param arquivoHistoricoWorkflow
     * @param conclusaoTarefa
     * @param acaoFeita
     * @param demanda
     */
    public HistoricoWorkflow(Tarefa tarefa, StatusHistorico status, ArquivoHistoricoWorkflow arquivoHistoricoWorkflow, Timestamp conclusaoTarefa, Tarefa acaoFeita, Demanda demanda) {
        this.tarefa = tarefa;
        this.status = status;
        this.arquivoHistoricoWorkflow = arquivoHistoricoWorkflow;
        this.conclusaoTarefa = conclusaoTarefa;
        this.acaoFeita = acaoFeita;
        this.demanda = demanda;
    }
}
