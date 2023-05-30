package br.weg.sade.dto;

import br.weg.sade.model.entities.Demanda;
import br.weg.sade.model.entities.Usuario;
import br.weg.sade.model.entities.enuns.Tarefa;
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
    private Usuario usuario;

    @NotNull
    private Tarefa acaoFeitaHistoricoAnterior;

    private String motivoDevolucaoAnterior;

    private Timestamp recebimento = new Timestamp(new Date().getTime());

    private Timestamp prazo = new Timestamp(new Date().getTime() + 86400000 * 5);


}
