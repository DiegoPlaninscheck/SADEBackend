package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Demanda;
import br.weg.sade.model.entity.Usuario;
import br.weg.sade.model.enums.StatusHistorico;
import br.weg.sade.model.enums.Tarefa;
import lombok.Getter;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
public class HistoricoWorkflowEdicaoDTO {

    @NotNull
    private Tarefa acaoFeita;

    @NotNull
    private StatusHistorico status = StatusHistorico.CONCLUIDO;

    private Demanda demanda;

    private Tarefa tarefa;

    private Usuario usuario;

    private Timestamp recebimento;

    private Timestamp prazo;

    private Timestamp conclusao = new Timestamp(new Date().getTime());

    private String motivoDevolucao;
}
