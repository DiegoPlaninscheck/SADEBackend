package br.weg.sade.model.dto;


import br.weg.sade.model.entity.Pauta;
import br.weg.sade.model.entity.Usuario;
import lombok.Data;

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


    private Date dataReuniao;

    @NotNull
    private Time inicioReuniao;

    @NotNull
    private Time finalReuniao;

    private List<Usuario> usuariosReuniaoATA;

    private Integer numeroAno;

    private Long numeroDG;

}
