package br.weg.sod.dto;

import lombok.NonNull;

public class DecisaoPropostaDTO {

    @NonNull
    private StatusComissao statusComissao;

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
