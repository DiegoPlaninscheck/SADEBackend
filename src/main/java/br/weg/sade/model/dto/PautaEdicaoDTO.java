package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Forum;
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

    private boolean teste = false;
}
