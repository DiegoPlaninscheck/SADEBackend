package br.weg.sod.dto;

import br.weg.sod.model.entities.Forum;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
public class PautaDTO {

    @Future
    private Date dataReuniao;

    @NotNull
    private Forum forum;
}
