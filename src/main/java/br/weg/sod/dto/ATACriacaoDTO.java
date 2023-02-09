package br.weg.sod.dto;


import br.weg.sod.model.entities.Pauta;
import br.weg.sod.model.entities.Usuario;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Getter
public class ATACriacaoDTO {

    @NotNull
    private Pauta pauta;

    @NotBlank
    private String tituloReuniaoATA;

    @NotNull
    private Date dataReuniao;

    @NotNull
    private Time inicioReuniao;

    @NotNull
    private Time finalReuniao;

    @NotNull
    private List<Usuario> usuariosReuniaoATA;

    private Integer numeroAno;

    private Long numeroDG;

}
