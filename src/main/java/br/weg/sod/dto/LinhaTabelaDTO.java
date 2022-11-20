package br.weg.sod.dto;

import lombok.NonNull;

public class LinhaTabelaDTO {

    @NonNull
    private Integer idLinhaTabela;

    @NonNull
    private String recurso;

    @NonNull
    private Integer valor;

    @NonNull
    private Double total;

    @NonNull
    private Integer idTabelaCusto;
}
