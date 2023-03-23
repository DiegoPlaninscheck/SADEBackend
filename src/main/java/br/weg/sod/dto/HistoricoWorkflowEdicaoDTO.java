package br.weg.sod.dto;

import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.Usuario;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import lombok.Getter;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
public class HistoricoWorkflowEdicaoDTO {

    @NotNull
    private Tarefa acaoFeita;

    @NotNull
    private StatusHistorico statusHistorico = StatusHistorico.CONCLUIDO;

    private Demanda demanda;

    private Tarefa tarefa;

    private Usuario usuario;

    private Timestamp recebimento;

    private Timestamp prazo;

    private Timestamp conclusao = new Timestamp(new Date().getTime());

    private String motivoDevolucao;
}
