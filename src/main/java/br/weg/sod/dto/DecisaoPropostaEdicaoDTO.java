package br.weg.sod.dto;

import br.weg.sod.model.entities.ATA;
import br.weg.sod.model.entities.Pauta;
import br.weg.sod.model.entities.Proposta;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
public class DecisaoPropostaEdicaoDTO {

    @NotNull
    private Pauta pauta;

    @NotNull
    private Proposta proposta;

    @NotNull
    private ATA ata;

    @NotNull
    private StatusDemanda statusDemandaComissao;

    @NotNull
    private Boolean ataPublicada;

    @Digits(integer = 100, fraction = 0)
    private Integer numeroSequencial;

    @Digits(integer = 10, fraction = 0)
    private Integer numeroAno;

    private String comentario;
}
