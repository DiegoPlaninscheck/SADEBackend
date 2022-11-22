package br.weg.sod.dto;

import br.weg.sod.model.entities.ArquivoDemanda;
import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.Usuario;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Getter
public class PropostaDTO {

    @Digits(integer = 10, fraction = 0)
    private Integer idProposta;

    @NotBlank
    private String escopo;

    @Positive
    private Double payback;

    @FutureOrPresent
    private Date periodoExecucaoInicio;

    @Future
    private Date periodoExecucaoFim;

    @NotNull
    private Boolean emWorkflow;

    @NotNull
    private Demanda demanda;

    private Boolean aprovadoWorkflow;

    private List<Usuario> responsaveisNegocio;

    private List<ArquivoDemanda> novosArquivos;
}
