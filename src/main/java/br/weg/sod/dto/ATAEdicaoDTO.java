package br.weg.sod.dto;

import br.weg.sod.model.entities.ArquivoPauta;
import lombok.Getter;

import java.util.List;

@Getter
public class ATAEdicaoDTO {

    private Integer numeroAno;

    private Integer numeroDG;

    private List<ArquivoPauta> arquivosPauta;

    private List<DecisaoPropostaATADTO> propostasAta;
}
