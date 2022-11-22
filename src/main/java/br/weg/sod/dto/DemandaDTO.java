package br.weg.sod.dto;

import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.Tamanho;
import lombok.NonNull;

import java.sql.Time;

public class DemandaDTO {

    @NonNull
    private Integer idDemanda;

    @NonNull
    private String tituloDemanda;

    @NonNull
    private StatusDemanda statusDemanda;

    @NonNull
    private Tamanho tamanho;

    @NonNull
    private String objetivo;

    @NonNull
    private String secaoTIResponsavel;

    @NonNull
    private String situacaoAtual;

    @NonNull
    private Integer frequenciaUso;

    @NonNull
    private Time prazoElaboracao;

    @NonNull
    private Integer codigoPPM;

    @NonNull
    private String linkJira;

    @NonNull
    private Double score;

    @NonNull
    private Integer idBUSolicitante;

    @NonNull
    private Integer idUsuario;

    @NonNull
    private Integer demandaIdDemanda;
}
