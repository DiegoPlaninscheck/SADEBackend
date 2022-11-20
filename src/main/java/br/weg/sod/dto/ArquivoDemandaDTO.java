package br.weg.sod.dto;

import lombok.NonNull;

public class ArquivoDemandaDTO {

    @NonNull
    private Integer idArquivoDemanda;

    @NonNull
    private byte[] arquivo;

    @NonNull
    private Integer idDemanda;

    @NonNull
    private Integer idUsuario;
}
