package br.weg.sod.dto;

import br.weg.sod.model.entities.Pauta;
import br.weg.sod.model.entities.Usuario;
import lombok.Data;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
public class ATAEdicaoDTO {

    private Integer idATA;

    private Pauta pauta;

    private String tituloReuniaoATA;

    private Integer numeroAno;

    private Long numeroDG;

    private Date dataReuniao;

    private Time inicioReuniao;

    private Time finalReuniao;

    private List<DecisaoPropostaATADTO> propostasAta;

    private List<Usuario> usuariosReuniaoATA;
}
