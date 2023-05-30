package br.weg.sade.dto;


import br.weg.sade.model.entities.Pauta;
import br.weg.sade.model.entities.Usuario;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
public class ATACriacaoDTO {

    @NotNull
    private Pauta pauta;

    @NotBlank
    private String tituloReuniaoATA;

    @FutureOrPresent
    private Date dataReuniao;

    @NotNull
    private Time inicioReuniao;

    @NotNull
    private Time finalReuniao;

    private List<Usuario> usuariosReuniaoATA;

    private Integer numeroAno;

    private Long numeroDG;

}
