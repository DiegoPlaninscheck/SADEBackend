package br.weg.sod.dto;

import br.weg.sod.model.entities.enuns.TipoDocumento;
import lombok.Data;

import java.util.List;

@Data
public class ATAEdicaoDTO {

    private Integer numeroAno;

    private Long numeroDG;

    private List<TipoDocumento> tipoDocumentos;

    private List<DecisaoPropostaATADTO> propostasAta;
}
