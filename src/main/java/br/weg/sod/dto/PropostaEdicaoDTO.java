package br.weg.sod.dto;

import br.weg.sod.model.entities.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PropostaEdicaoDTO {

    private Integer idProposta;

    private String escopo;

    private Date periodoExecucaoInicio;

    private Date periodoExecucaoFim;

    private Boolean emWorkflow;

    private Boolean aprovadoWorkflow;

    private Integer payback;

    private List<Usuario> responsaveisNegocio;

    private List<TabelaCusto> tabelasCustoProposta;
}
