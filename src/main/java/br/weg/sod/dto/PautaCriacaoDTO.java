package br.weg.sod.dto;

import br.weg.sod.model.entities.Forum;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
public class PautaCriacaoDTO {

    @Future
    private Date dataReuniao;

    @NotNull
    private Forum forum;

    private List<DecisaoPropostaPautaCriacaoDTO> propostasPauta;
}
