package br.weg.sod.dto;

import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.Usuario;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import lombok.Getter;

import javax.validation.constraints.*;
import java.sql.Timestamp;

@Getter
public class HistoricoWorkflowEdicaoDTO {

    @FutureOrPresent
    private Timestamp recebimento;

    @Future
    private Timestamp prazo;

    @NotNull
    private String tarefa;

    @NotNull
    private StatusHistorico statusHistorico;

    @NotNull
    private Usuario usuario;

    @NotNull
    private Demanda demanda;

    //ver como vai fazer pdf
//    @NotNull
//    private byte[] pdfHistorico;

    private String motivoDevolucao;

    private Timestamp conclusao;

    private String acaoFeita;
}
