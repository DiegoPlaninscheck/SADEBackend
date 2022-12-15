package br.weg.sod.dto;

import br.weg.sod.model.entities.DecisaoProposta;
import br.weg.sod.model.entities.Forum;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
public class PautaDTO {

    @Future
    private Date dataReuniao;

    @NotNull
    private Forum forum;

    @Size
    private List<DecisaoProposta> propostasPauta;
}
