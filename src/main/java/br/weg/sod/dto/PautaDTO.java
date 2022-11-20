package br.weg.sod.dto;

import lombok.NonNull;

import java.util.Date;

public class PautaDTO {

    @NonNull
    private Integer idPauta;

    @NonNull
    private Date dataReuniao;

    @NonNull
    private Integer idForum;
}
