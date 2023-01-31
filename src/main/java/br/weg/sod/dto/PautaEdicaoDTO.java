package br.weg.sod.dto;

import br.weg.sod.model.entities.Forum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PautaEdicaoDTO {

    private Date dataReuniao;

    private Forum forum;

    private List<DecisaoPropostaPautaEdicaoDTO> propostasPauta;
}
