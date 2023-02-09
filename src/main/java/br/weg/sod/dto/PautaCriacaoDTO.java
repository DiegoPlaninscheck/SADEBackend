package br.weg.sod.dto;

import br.weg.sod.model.entities.Forum;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Getter
public class PautaCriacaoDTO {

    @NotBlank
    private String tituloReuniaoPauta;

    @Future
    private Date dataReuniao;

    @NotNull
    private Time inicioReuniao;

    @NotNull
    private Time finalReuniao;

    @NotNull
    private Forum forum;

    private List<DecisaoPropostaPautaCriacaoDTO> propostasPauta;
}
