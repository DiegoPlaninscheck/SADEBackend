package br.weg.sod.dto;

import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.Tamanho;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Data
public class DemandaEdicaoDTO {

    @NotBlank
    private String tituloDemanda;

    @NotNull
    private StatusDemanda statusDemanda;

    @NotBlank
    private String objetivo;

    @NotBlank
    private String situacaoAtual;

    @Digits(integer = 3, fraction = 0)
    private Integer frequenciaUso;

    @PositiveOrZero
    private Double score;

    @NotNull
    private Usuario usuario;

    @NotNull
    private Tamanho tamanho;

    @NotNull
    private BU BUSolicitante;

    @NotNull
    private List<BU> BUsBeneficiadas;

    @NotBlank
    private String secaoTIResponsavel;

    private Date prazoElaboracao;

    private Integer codigoPPM;

    private String linkJira;

    private List<CentroCusto> centroCustoDemanda;

    private List<Beneficio> beneficiosDemanda;
}
