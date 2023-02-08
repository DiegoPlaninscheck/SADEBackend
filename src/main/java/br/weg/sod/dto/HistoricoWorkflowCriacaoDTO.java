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
public class HistoricoWorkflowCriacaoDTO {

    @NotNull
    private Tarefa tarefa;

    @NotNull
    private Demanda demanda;

    @NotNull
    private Tarefa acaoFeitaHistoricoAnterior;

    private Timestamp recebimento = new Timestamp(new Date().getTime());

    private Timestamp prazo = new Timestamp(new Date().getTime() + 86400000 * 5);

    private Usuario usuario;

}
