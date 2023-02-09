package br.weg.sod.dto;

import br.weg.sod.model.entities.enuns.TipoDocumento;
import lombok.Data;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
public class ATAEdicaoDTO {

    private Integer numeroAno;

    private Long numeroDG;

    private Date dataReuniao;

    private Time inicioReuniao;

    private Time finalReuniao;

    private List<TipoDocumento> tipoDocumentos;

    private List<DecisaoPropostaATADTO> propostasAta;
}
