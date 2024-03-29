package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Forum;
import br.weg.sade.model.entity.Proposta;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;
import java.util.List;

//@Getter
@Data
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

    private List<Proposta> propostasPauta;

    private boolean teste = false;
}
