package br.weg.sod.dto;

import br.weg.sod.model.entities.Forum;
import lombok.Data;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
public class PautaEdicaoDTO {

    private String tituloReuniaoPauta;

    private Date dataReuniao;

    private Time inicioReuniao;

    private Time finalReuniao;

    private Forum forum;

    private List<DecisaoPropostaPautaEdicaoDTO> propostasPauta;

    private Date dataReuniaoATA;

    private boolean teste = false;
}
