package br.weg.sod.dto;

import br.weg.sod.model.entities.*;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
public class PropostaEdicaoDTO {

    @NotBlank
    private String escopo;

    @FutureOrPresent
    private Date periodoExecucaoInicio;

    @Future
    private Date periodoExecucaoFim;

    private Boolean emWorkflow;

    private Boolean aprovadoWorkflow;

    private Integer payback;

    private List<Usuario> responsaveisNegocio;

    private List<TabelaCusto> tabelasCustoProposta;
}
