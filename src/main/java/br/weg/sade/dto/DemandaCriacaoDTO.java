package br.weg.sade.dto;

import br.weg.sade.model.entities.*;
import br.weg.sade.model.entities.enuns.Frequencia;
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

    @NotNull
    private Frequencia frequenciaUso;

    @Size
    private List<CentroCusto> centroCustoDemanda;

    private List<BeneficioDTO> beneficiosDemanda;

    @NotNull
    private Usuario usuario;

    private Double score;

    private boolean rascunho = false;

    private boolean devolvida = false;
}
