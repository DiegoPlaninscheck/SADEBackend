package br.weg.sod.dto;

import br.weg.sod.model.entities.*;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
public class PropostaDTO {

    @NotBlank
    private String escopo;

    @FutureOrPresent
    private Date periodoExecucaoInicio;

    @Future
    private Date periodoExecucaoFim;

    @NotNull
    private Boolean emWorkflow;

    @NotNull
    private Demanda demanda;

    @NotNull
    private Boolean aprovadoWorkflow;

    private Integer payback;

    private List<Usuario> responsaveisNegocio;

    private List<TabelaCusto> tabelasCustoProposta;
}
