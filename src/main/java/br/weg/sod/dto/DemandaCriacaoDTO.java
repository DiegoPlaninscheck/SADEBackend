package br.weg.sod.dto;

import br.weg.sod.model.entities.*;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.*;

import java.util.List;

@ToString
@Data
public class DemandaCriacaoDTO {

    @NotBlank
    private String tituloDemanda;

    @NotBlank
    private String objetivo;

    @NotBlank
    private String situacaoAtual;

    @Digits(integer = 3, fraction = 0)
    private Integer frequenciaUso;

    @Size
    private List<CentroCusto> centroCustoDemanda;

    private List<BeneficioDTO> beneficiosDemanda;

    @NotNull
    private Usuario usuario;

    private Double score;

    private boolean rascunho = false;
}
