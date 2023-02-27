package br.weg.sod.dto;

import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.Usuario;
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

    private Integer payback;
}

