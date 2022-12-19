package br.weg.sod.dto;

import br.weg.sod.model.entities.ArquivoPauta;
import br.weg.sod.model.entities.Forum;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class PautaEdicaoDTO {

    private Date dataReuniao;

    private Forum forum;

    private List<ArquivoPauta> arquivosPauta;

    private List<DecisaoPropostaPautaEdicaoDTO> propostasPauta;
}
