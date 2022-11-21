package br.weg.sod.model.entities;

import br.weg.sod.model.entities.enuns.Status;
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
    @Column
    private Integer idHistoricoWorkflow;

    @Column(nullable = false)
    private Timestamp recebimento;

    @Column(nullable = false)
    private Timestamp prazo;

    @Column(nullable = false)
    private Tarefa tarefa;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private byte[] pdfHistorico;

    @Column
    private String motivoDevolucao;

    @Column
    private Timestamp conclusaoTarefa;

    @Column
    private Tarefa acaoFeita;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda idDemanda;
}
