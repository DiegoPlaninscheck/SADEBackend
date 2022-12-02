package br.weg.sod.dto;

import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
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

    @PositiveOrZero
    private Double score;

    @NotNull
    private List<CentroCusto> centroCustoDemanda;

    private List<Beneficio> beneficiosDemanda;

    private List<BU> BUsBeneficiadas;

    @NotNull
    private Usuario usuario;
}
