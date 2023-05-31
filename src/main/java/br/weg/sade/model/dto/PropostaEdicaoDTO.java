package br.weg.sade.model.dto;

import br.weg.sade.model.entity.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PropostaEdicaoDTO {

    private Integer idProposta;

    private String escopo;

    private Date periodoExecucaoInicio;

    private Date periodoExecucaoFim;

    private Demanda demanda;

    private Boolean emWorkflow;

    private Boolean aprovadoWorkflow;

    private Boolean estaEmPauta;

    private Boolean avaliadoWorkflow;

    private String payback;

    private List<Usuario> responsaveisNegocio;

    private List<TabelaCusto> tabelasCustoProposta;
}
