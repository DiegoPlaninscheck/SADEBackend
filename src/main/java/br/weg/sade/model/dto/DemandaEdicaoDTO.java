package br.weg.sade.model.dto;

import br.weg.sade.model.entity.*;
import br.weg.sade.model.enums.Frequencia;
import br.weg.sade.model.enums.SecaoTI;
import br.weg.sade.model.enums.StatusDemanda;
import br.weg.sade.model.enums.Tamanho;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class DemandaEdicaoDTO {

    private Integer idDemanda;

    private String tituloDemanda;

    private StatusDemanda statusDemanda;

    private String objetivo;

    private String situacaoAtual;

    private Frequencia frequenciaUso;

    private Double score;

    private Usuario usuario;

    private Tamanho tamanho;

    private BU BUSolicitante;

    private List<BU> BUsBeneficiadas;

    private SecaoTI secaoTIResponsavel;

    private Date prazoElaboracao;

    private Integer codigoPPM;

    private String linkJira;

    private List<CentroCusto> centroCustoDemanda;

    private List<Beneficio> beneficiosDemanda;

    private List<ArquivoDemanda> arquivosDemanda;

    private Boolean pertenceUmaProposta = false;

    private Boolean temChat = false;

    private Boolean rascunho = false;

    private Boolean devolvida = false;

    private Boolean criandoDemandaPorRascunho = false;

    private Boolean editandoDemanda = false;

    private Boolean classificando = false;

    private Boolean adicionandoInformacoes = false;
}
