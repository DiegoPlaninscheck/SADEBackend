package br.weg.sod.dto;

import br.weg.sod.model.entities.Status;
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
    private Status status;

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
