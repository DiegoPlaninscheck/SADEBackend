package br.weg.sod.dto;

import br.weg.sod.model.entities.Beneficio;
import br.weg.sod.model.entities.CentroCusto;
import br.weg.sod.model.entities.Usuario;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Getter
public class DemandaCriacaoDTO {

    @NotBlank
    private String tituloDemanda;

    @NotBlank
    private String objetivo;

    @NotBlank
    private String situacaoAtual;

    @Digits(integer = 3, fraction = 0)
    private Integer frequenciaUso;

    @PositiveOrZero
    private Double score;

    @NotNull
    private List<CentroCusto> centrosCustoDemanda;

    private List<Beneficio> beneficiosDemanda;

    @NotNull
    private Usuario usuario;
}
