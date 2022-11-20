package br.weg.sod.dto;

import lombok.NonNull;

public class TabelaCustoDTO {

    @NonNull
    private Integer idTabelaCusto;

    @NonNull
    private String nome;

    @NonNull
    private Integer valor;

    @NonNull
    private Double total;

    @NonNull
    private Integer idProposta;
}
