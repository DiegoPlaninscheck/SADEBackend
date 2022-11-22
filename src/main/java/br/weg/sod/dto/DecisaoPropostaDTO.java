package br.weg.sod.dto;

import br.weg.sod.model.entities.enuns.StatusDemanda;
import lombok.NonNull;

public class DecisaoPropostaDTO {

    @NonNull
    private StatusDemanda statusDemandaComissao;

    @NonNull
    private Boolean ataPublicada;

    @NonNull
    private String comentario;

    @NonNull
    private Integer numeroSequencial;

    @NonNull
    private Integer idProposta;

    @NonNull
    private Integer idPauta;

    @NonNull
    private Integer idATA;
}
