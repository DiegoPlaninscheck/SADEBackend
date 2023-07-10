package br.weg.sade.controller;

import br.weg.sade.model.entity.*;
import br.weg.sade.model.enums.AcaoNotificacao;
import br.weg.sade.model.enums.StatusHistorico;
import br.weg.sade.model.enums.Tarefa;
import br.weg.sade.model.enums.TipoNotificacao;
import br.weg.sade.service.*;
import br.weg.sade.util.PDFUtil;
import br.weg.sade.util.PropostaUtil;
import br.weg.sade.util.UtilFunctions;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sade/proposta")
public class PropostaController {

    private PropostaService propostaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;
    private DemandaService demandaService;
    private TabelaCustoService tabelaCustoService;
    private CentroCustoService centroCustoService;
    private NotificacaoService notificacaoService;
    private SimpMessagingTemplate simpMessagingTemplate;

    private EmailSenderService emailSenderService;

    @GetMapping
    public ResponseEntity<List<Proposta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(propostaService.findAll());
    }

    @GetMapping("/pauta/{estaEmPauta}")
    public ResponseEntity<List<Proposta>> findByEstaEmPauta(@PathVariable(name = "estaEmPauta") Boolean estaEmPauta) {
        return ResponseEntity.status(HttpStatus.OK).body(propostaService.findByEstaEmPauta(estaEmPauta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idProposta) {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(propostaService.findById(idProposta));
    }

    @Transactional
    @PostMapping("/{idAnalista}")
    public ResponseEntity<Object> save(
            @RequestParam("proposta") @Valid String propostaJSON,
            @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles,
            @PathVariable("idAnalista") Integer idAnalista) throws IOException {
        PropostaUtil util = new PropostaUtil();
        Proposta proposta = util.convertJsonToModel(propostaJSON, 1);

        ResponseEntity<Object> validacao = validacoesProposta(proposta, idAnalista);

        if (validacao != null) {
            return validacao;
        }

//        if (proposta.getPayback() == null) {
//            String valorPayback = 2; //depois fazer a conta com payback e custo totais e os caralho
//            proposta.setPayback(valorPayback);
//        }

        proposta.setIdProposta(proposta.getDemanda().getIdDemanda());
        Usuario analistaResponsavel = usuarioService.findById(idAnalista).get();

        Proposta propostaSalva = propostaService.save(proposta);
        Demanda demandaProposta = demandaService.findById(propostaSalva.getIdProposta()).get();

        PDFUtil pdfUtil = new PDFUtil();
        ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = null;

        try {
            arquivoHistoricoWorkflow = pdfUtil.criarPDFProposta(propostaSalva);
        } catch (Exception e) {
            System.out.println(e);
        }

        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                demandaProposta.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, analistaResponsavel));
            }
        }

        demandaProposta.setPertenceUmaProposta(true);
        demandaService.save(demandaProposta);

        GerenteNegocio gerenteNegocio = usuarioService.findGerenteByDepartamento(demandaProposta.getUsuario().getDepartamento());

        // Notificação Solicitante
        Notificacao notificacaoSolicitante = new Notificacao();
        notificacaoSolicitante.setAcao(AcaoNotificacao.VIROUPROPOSTA);
        notificacaoSolicitante.setDescricaoNotificacao("Sua demanda virou uma proposta");
        notificacaoSolicitante.setTituloNotificacao("Virou uma proposta");
        notificacaoSolicitante.setTipoNotificacao(TipoNotificacao.PROPOSTA);
        notificacaoSolicitante.setLinkNotificacao("http://localhost:8081/notifications/proposal");
        notificacaoSolicitante.setIdComponenteLink(propostaSalva.getIdProposta());

        List<Usuario> usuariosSolicitante = new ArrayList<>();

        usuariosSolicitante.add(demandaProposta.getUsuario());

        notificacaoSolicitante.setUsuariosNotificacao(usuariosSolicitante);

        notificacaoSolicitante = notificacaoService.save(notificacaoSolicitante);

        simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + demandaProposta.getIdDemanda(), notificacaoSolicitante);

        // Notificação Gerente de Negocio do Solicitante
        Notificacao notificacaoGerenteNegocio = new Notificacao();
        notificacaoGerenteNegocio.setAcao(AcaoNotificacao.VIROUPROPOSTA);
        notificacaoGerenteNegocio.setDescricaoNotificacao("A demanda virou uma proposta");
        notificacaoGerenteNegocio.setTituloNotificacao("Virou uma proposta");
        notificacaoGerenteNegocio.setTipoNotificacao(TipoNotificacao.PROPOSTA);
        notificacaoGerenteNegocio.setLinkNotificacao("http://localhost:8081/notifications/proposal");
        notificacaoGerenteNegocio.setIdComponenteLink(propostaSalva.getIdProposta());

        List<Usuario> usuariosGerenteNegocio = new ArrayList<>();

        usuariosGerenteNegocio.add(gerenteNegocio);

        notificacaoGerenteNegocio.setUsuariosNotificacao(usuariosGerenteNegocio);

        notificacaoGerenteNegocio = notificacaoService.save(notificacaoGerenteNegocio);

        simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + demandaProposta.getIdDemanda(), notificacaoGerenteNegocio);

        //encerra o histórico da criação de proposta
        historicoWorkflowService.finishHistoricoByDemanda(propostaSalva.getDemanda(), Tarefa.CRIARPROPOSTA, analistaResponsavel, null, arquivoHistoricoWorkflow);

        //inicia o histórico de criar pauta
        historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARPAUTA, StatusHistorico.EMANDAMENTO, analistaResponsavel, propostaSalva.getDemanda());

        return ResponseEntity.status(HttpStatus.OK).body(propostaSalva);
    }

    @PutMapping("/{idProposta}/{idAnalista}")
    public ResponseEntity<Object> edit(
            @RequestParam("proposta") @Valid String propostaJSON,
            @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles,
            @RequestParam(value = "pdfVersaoHistorico", required = false) MultipartFile versaoPDF,
            @PathVariable(name = "idProposta") Integer idProposta,
            @PathVariable(name = "idAnalista") Integer idAnalista)
            throws IOException {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }

        PropostaUtil util = new PropostaUtil();
        Proposta proposta = util.convertJsonToModel(propostaJSON, 2);
        Proposta propostaDB = propostaService.findById(idProposta).get();
        BeanUtils.copyProperties(proposta, propostaDB, UtilFunctions.getPropriedadesNulas(proposta));

        ResponseEntity<Object> validacaoEdicao = validacoesItensProposta(proposta, idAnalista, versaoPDF);

        if (validacaoEdicao != null) {
            return validacaoEdicao;
        }

        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                proposta.getDemanda().getArquivosDemanda().add(new ArquivoDemanda(multipartFile, usuarioService.findById(idAnalista).get()));
            }
        }

        Proposta propostaSalva = propostaService.save(propostaDB);


        Usuario usuarioAprovacao = usuarioService.findById(idAnalista).get();

        if (proposta.getEmWorkflow() != null) {
            if (proposta.getEmWorkflow()) {
                //encerra historico de criar pauta
                Usuario analistaResponsavel = usuarioService.findById(idAnalista).get();
                historicoWorkflowService.finishHistoricoByDemanda(propostaSalva.getDemanda(), Tarefa.INICIARWORKFLOW, analistaResponsavel, null, null);

                //inicia histórico de em workflow
                Usuario solicitante = usuarioService.findById(propostaSalva.getDemanda().getUsuario().getIdUsuario()).get();
                GerenteNegocio gerenteNegocio = usuarioService.findGerenteByDepartamento(solicitante.getDepartamento());

                historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.AVALIARWORKFLOW, StatusHistorico.EMANDAMENTO, gerenteNegocio, propostaSalva.getDemanda());

                Notificacao notificacao = new Notificacao();

                List<Usuario> usuarios = new ArrayList<>();
                usuarios.add(gerenteNegocio);
                notificacao.setAcao(AcaoNotificacao.NOVOWORKFLOWAPROVACAO);
                notificacao.setDescricaoNotificacao("O analista iniciou um novo workflow de aprovação!");
                notificacao.setTituloNotificacao("Novo workflow iniciado");
                notificacao.setTipoNotificacao(TipoNotificacao.PROPOSTA);
                notificacao.setLinkNotificacao("http://localhost:8081/notifications/proposal");
                notificacao.setIdComponenteLink(propostaSalva.getDemanda().getIdDemanda());
                notificacao.setUsuariosNotificacao(usuarios);
                notificacao = notificacaoService.save(notificacao);
                simpMessagingTemplate.convertAndSend("/notificacao/demanda/" +
                        propostaSalva.getDemanda().getIdDemanda(), notificacao);

                emailSenderService.sendEmail("diego_planinscheck@estudante.sc.senai.br", "Gerente Negócio",
                        "Você tem um novo workflow de aprovação para analisar\n" +
                                "Segue link para aprovação: http://localhost:8081/home/proposal/?" + propostaSalva.getIdProposta());
            }
        } else if (usuarioAprovacao instanceof GerenteTI) {
            //encerra historico de avaliação do gerente de TI
            if (proposta.getAprovadoWorkflow()) {
                historicoWorkflowService.finishHistoricoByDemanda(propostaSalva.getDemanda(), Tarefa.APROVARWORKFLOW, usuarioAprovacao, null, null);
            } else {
                historicoWorkflowService.finishHistoricoByDemanda(propostaSalva.getDemanda(), Tarefa.REPROVARWORKFLOW, usuarioAprovacao, null, null);
            }

            //inicia histórico de criação de pauta
            AnalistaTI analistaResponsavel = historicoWorkflowService.findLastHistoricoAnalistaByDemanda(propostaDB.getDemanda());
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARPAUTA, StatusHistorico.EMANDAMENTO, analistaResponsavel, propostaSalva.getDemanda());
        }
//
        return ResponseEntity.status(HttpStatus.OK).body(propostaSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idProposta) {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }

        propostaService.deleteById(idProposta);
        return ResponseEntity.status(HttpStatus.OK).body("Proposta deletada com sucesso!");
    }

    private ResponseEntity<Object> validacoesProposta(Proposta proposta, Integer idAnalista) {
        if (proposta.getPeriodoExecucaoFim() != null) {
            if (proposta.getPeriodoExecucaoFim().getTime() < proposta.getPeriodoExecucaoInicio().getTime()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("O período de execução inválido");
            }
        }

        if (usuarioService.findById(idAnalista).get() instanceof Solicitante || usuarioService.findById(idAnalista).get() instanceof GerenteNegocio) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ID de usuário informado inválido para essa ação");
        }

        List<Usuario> responsaveisNegocio = proposta.getResponsaveisNegocio();

        if (responsaveisNegocio != null && responsaveisNegocio.size() != 0) {
            if (!usuarioService.responsaveisValidos(responsaveisNegocio)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de álgum responsável pela proposta informado inválido");
            }
        }

        List<TabelaCusto> tabelasCustoProposta = proposta.getTabelasCustoProposta();

        if (tabelasCustoProposta != null) {
            if (tabelasCustoProposta.size() != 0) {
                if (!propostaService.tabelasvalidas(tabelasCustoProposta)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tabelas de custo contém informações inválidas");
                }

                for (TabelaCusto tabelaCusto : tabelasCustoProposta) {
                    List<CentroCustoPagante> centroDeCustos = tabelaCusto.getCentrosCustoPagantes();

                    if (centroDeCustos != null && centroDeCustos.size() != 0) {
                        for (CentroCustoPagante centroCustoPagante : centroDeCustos) {

                            if (!centroCustoService.existsById(centroCustoPagante.getCentroCusto().getIdCentroCusto())) {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de centro de custo informado inválido");
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private ResponseEntity<Object> validacoesItensProposta(Proposta proposta, Integer idAnalista, MultipartFile versaoPDF) {
        ResponseEntity<Object> validacaoProposta = validacoesProposta(proposta, idAnalista);

        if (propostaAtualizou(proposta)) {
            if (versaoPDF == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("PDF da versão não informado");
            } else if (versaoPDF.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("PDF da versão não informado");
            }
        }

        if (proposta.getAprovadoWorkflow() != null) {
            if (!(usuarioService.findById(idAnalista).get() instanceof GerenteTI)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário responsável não pode ser encarregado dessa ação");
            }
        }

        if (validacaoProposta != null) {
            return validacaoProposta;
        }

        List<TabelaCusto> tabelasCustoProposta = proposta.getTabelasCustoProposta();

        if (tabelasCustoProposta != null) {
            if (tabelasCustoProposta.size() != 0) {
                for (TabelaCusto tabelaCusto : proposta.getTabelasCustoProposta()) {

                    if (!tabelaCustoService.existsById(tabelaCusto.getIdTabelaCusto())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("ID de tabela de custo inválido");
                    }

                    List<CentroCustoPagante> centroDeCustos = tabelaCusto.getCentrosCustoPagantes();

                    if (centroDeCustos != null && centroDeCustos.size() != 0) {
                        for (CentroCustoPagante centroCustoPagante : centroDeCustos) {
                            if (centroCustoPagante.getIdCentroCustoPagante() == null) {
                                if (centroDeCustos.size() != tabelaCustoService.findById(tabelaCusto.getIdTabelaCusto()).get().getCentrosCustoPagantes().size() + 1) {
                                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Se for adicionar um centro de custo pagante deverá atualizar as porcentagens das despesas de todos relacionados áquela proposta");
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean propostaAtualizou(Proposta proposta) {
        if (proposta.getPayback() != null) {
            return true;
        }

        if (proposta.getTabelasCustoProposta() != null) {
            return true;
        }

        if (proposta.getResponsaveisNegocio() != null) {
            return true;
        }

        if (proposta.getPeriodoExecucaoInicio() != null) {
            return true;
        }

        if (proposta.getPeriodoExecucaoFim() != null) {
            return true;
        }

        return false;
    }

}
