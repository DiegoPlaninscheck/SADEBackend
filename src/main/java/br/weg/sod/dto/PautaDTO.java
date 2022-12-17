package br.weg.sod.dto;

import br.weg.sod.model.entities.DecisaoPropostaPauta;
import br.weg.sod.model.entities.Forum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
public class PautaDTO {

    @Future
    private Date dataReuniao;

    @NotNull
    private Forum forum;

    private byte[] ataReuniao;

    private List<DecisaoPropostaPauta> propostasPauta;
}
