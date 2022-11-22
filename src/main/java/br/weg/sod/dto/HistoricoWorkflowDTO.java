package br.weg.sod.dto;

import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.NonNull;

import java.sql.Timestamp;

public class HistoricoWorkflowDTO {

    @NonNull
    private Integer idHistoricoWorkflow;

    @NonNull
    private Timestamp recebimento;

    @NonNull
    private Timestamp prazo;

    @NonNull
    private String tarefa;

    @NonNull
    private StatusDemanda statusDemanda;

    @NonNull
    private byte[] pdfHistorico;

    @NonNull
    private String motivoDevolucao;

    @NonNull
    private Timestamp conclusao;

    @NonNull
    private String acaoFeita;

    @NonNull
    private Integer idUsuario;

    @NonNull
    private Integer idDemanda;
}
