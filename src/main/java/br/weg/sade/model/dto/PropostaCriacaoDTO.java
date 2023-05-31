package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Demanda;
import br.weg.sade.model.entity.Usuario;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class PropostaCriacaoDTO {

    @NotBlank
    private String escopo;

    @FutureOrPresent
    private Date periodoExecucaoInicio;

    @Future
    private Date periodoExecucaoFim;

    @NotNull
    private Demanda demanda;

    @NotNull
    private List<Usuario> responsaveisNegocio;

    @NotNull
    private List<TabelaCustoDTO> tabelasCustoProposta;

    private String payback;
}

