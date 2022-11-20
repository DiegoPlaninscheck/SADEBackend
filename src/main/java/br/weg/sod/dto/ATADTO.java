package br.weg.sod.dto;

import lombok.NonNull;

public class ATADTO {

    @NonNull
    private Integer idATA;

    @NonNull
    private byte[] pdf;

    @NonNull
    private Integer numeroDG;

    @NonNull
    private byte[] documentoAprovacao;

}
