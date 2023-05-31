package br.weg.sade.controller;

import br.weg.sade.model.dto.DecisaoPropostaPautaEdicaoDTO;
import br.weg.sade.model.dto.PautaCriacaoDTO;
import br.weg.sade.model.dto.PautaEdicaoDTO;
import br.weg.sade.model.entity.*;
import br.weg.sade.model.enuns.AcaoNotificacao;
import br.weg.sade.model.enuns.StatusHistorico;
import br.weg.sade.model.enuns.Tarefa;
import br.weg.sade.model.enuns.TipoNotificacao;
import br.weg.sade.service.*;
import br.weg.sade.util.PDFUtil;
import br.weg.sade.util.PautaUtil;
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
@RequestMapping("/sade/pauta")
public class PautaController {

    private PautaService pautaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;
    private DecisaoPropostaPautaService decisaoPropostaPautaService;
    private PropostaService propostaService;
    private ForumService forumService;
    private NotificacaoService notificacaoService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public ResponseEntity<List<Pauta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findAll());
    }

    @GetMapping("/criarATA")
    public ResponseEntity<List<Pauta>> findAllForATA() {
        List<Pauta> listaPautas = pautaService.findPautasByPertenceUmaATA(false), listaPautasPossiveisCriar = new ArrayList<>();

        for (Pauta pauta : listaPautas) {
            if (pauta.getPropostasPauta().size() > 0) {
                if (pauta.getPropostasPauta().get(0).getAtaPublicada() != null) {
                    listaPautasPossiveisCriar.add(pauta);
                }
            }
        }

        return ResponseEntity.ok().body(listaPautasPossiveisCriar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findById(idPauta));
    }

    @GetMapping("/arquivos/{id}")
    public ResponseEntity<Object> findArquivosById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findById(idPauta).get().getArquivosPauta());
    }

    @GetMapping("/arquivos/pautas")
    public ResponseEntity<Object> findArquivosPautas() {
        List<Pauta> pautas = pautaService.findAll();

        List<ArquivoPauta> arquivoPautas = new ArrayList();

        for (Pauta pauta : pautas) {
            for (ArquivoPauta arquivoPauta : pauta.getArquivosPauta()) {
                arquivoPautas.add(arquivoPauta);
            }
        }

        return ResponseEntity.ok().body(arquivoPautas);
    }

    @Transactional
    @PostMapping("/{idAnalista}")
    public ResponseEntity<Object> save(
            @RequestBody @Valid PautaCriacaoDTO pautaCriacaoDTO,
            @PathVariable(name = "idAnalista") Integer idAnalista)
            throws IOException {

        if (!validacaoPropostasCriacao(pautaCriacaoDTO.getPropostasPauta())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Uma das propostas informadas já está em uma pauta ou o id informado não existe");
        }

        if (!forumService.existsById(pautaCriacaoDTO.getForum().getIdForum())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("O id do fórum informado não existe");
        }

        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(pautaCriacaoDTO, pauta);

        for (Proposta proposta : pautaCriacaoDTO.getPropostasPauta()) {
            DecisaoPropostaPauta decisaoPropostaPauta = new DecisaoPropostaPauta();
            decisaoPropostaPauta.setProposta(proposta);
            pauta.getPropostasPauta().add(decisaoPropostaPauta);

            Proposta propostaDaPauta = propostaService.findById(decisaoPropostaPauta.getProposta().getIdProposta()).get();
            propostaDaPauta.setEstaEmPauta(true);
            propostaService.save(propostaDaPauta);
        }

        Pauta pautaSalva = pautaService.save(pauta);

        List<Usuario> usuariosNotificacao = new ArrayList<>();

        Notificacao notificacao = new Notificacao();
        notificacao.setAcao(AcaoNotificacao.VIROUPAUTA);
        notificacao.setDescricaoNotificacao("A proposta foi adicionada a uma pauta");
        notificacao.setTituloNotificacao("Pauta criada");
        notificacao.setTipoNotificacao(TipoNotificacao.PAUTA);
        notificacao.setLinkNotificacao("http://localhost:8081/home/agenda");
        notificacao.setIdComponenteLink(pautaSalva.getIdPauta());

        if (!pautaCriacaoDTO.isTeste()) {
            for (DecisaoPropostaPauta decisaoPropostaPauta : pauta.getPropostasPauta()) {

                Usuario gerenteNegocio = usuarioService.findGerenteByDepartamento(decisaoPropostaPauta.getProposta().
                        getDemanda().getUsuario().getDepartamento());

                usuariosNotificacao.add(decisaoPropostaPauta.getProposta().getDemanda().getUsuario());
                usuariosNotificacao.add(gerenteNegocio);

                System.out.println("Usuarios: " + usuariosNotificacao);
                notificacao.setUsuariosNotificacao(usuariosNotificacao);
                System.out.println("UsuariosNotificacao: " + notificacao.getUsuariosNotificacao());

//            encerrar historico criar pauta
                Demanda demandaDecisao = propostaService.findById(decisaoPropostaPauta.getProposta().getIdProposta()).get().getDemanda();
                Usuario analistaResponsavel = usuarioService.findById(idAnalista).get();
                historicoWorkflowService.finishHistoricoByDemanda(demandaDecisao, Tarefa.CRIARPAUTA, analistaResponsavel, null, null);

//            inicio informar parecer da comissao
                historicoWorkflowService.initializeHistoricoByDemanda(
                        new Timestamp(pauta.getDataReuniao().getTime()),
                        Tarefa.INFORMARPARECERFORUM,
                        StatusHistorico.EMAGUARDO,
                        analistaResponsavel,
                        demandaDecisao
                );
            }
        }

        for (DecisaoPropostaPauta decisaoPropostaPauta : pauta.getPropostasPauta()) {
            simpMessagingTemplate.convertAndSend("/notificacao/demanda/" +
                    decisaoPropostaPauta.getProposta().getDemanda().getIdDemanda(), notificacao);
        }
        notificacaoService.save(notificacao);

        return ResponseEntity.status(HttpStatus.OK).body(pautaSalva);
    }

    @PutMapping("/{idPauta}/{idAnalista}")
    public ResponseEntity<Object> edit(
            @RequestParam("pauta") @Valid String pautaJSON,
            @RequestParam(value = "arquivos", required = false) MultipartFile multipartFile,
            @PathVariable(name = "idPauta") Integer idPauta,
            @PathVariable(name = "idAnalista") Integer idAnalista)
            throws IOException {
        System.out.println("ID Pauta: " + idPauta);
        System.out.println("ID Analista: " + idAnalista);
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }

        PautaUtil util = new PautaUtil();
        Pauta pauta = pautaService.findById(idPauta).get();
        PautaEdicaoDTO pautaDTO = util.convertJsontoDto(pautaJSON);

        BeanUtils.copyProperties(pautaDTO, pauta, UtilFunctions.getPropriedadesNulas(pautaDTO));
        pauta.setIdPauta(idPauta);
        Usuario analistaTIresponsavel = usuarioService.findById(idAnalista).get();

        if (multipartFile != null) {
            if (multipartFile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("PDF a ata da reunião não informado");
            }
            List<ArquivoPauta> arquivosPauta = new ArrayList<>();

            arquivosPauta.add(new ArquivoPauta(multipartFile, analistaTIresponsavel));

            pauta.setArquivosPauta(arquivosPauta);
        }

        List<DecisaoPropostaPauta> decisoesPauta = new ArrayList<>();

        for (DecisaoPropostaPautaEdicaoDTO decisaoDTO : pautaDTO.getPropostasPauta()) {
            DecisaoPropostaPauta decisaoPropostaPautaNova = decisaoPropostaPautaService.findById(decisaoDTO.getIdDecisaoPropostaPauta()).get();
            Proposta propostaDaDecisao = decisaoPropostaPautaNova.getProposta();

            BeanUtils.copyProperties(decisaoDTO, decisaoPropostaPautaNova);

            if (decisaoPropostaPautaNova.getProposta() == null) {
                decisaoPropostaPautaNova.setProposta(propostaDaDecisao);
            }

            decisoesPauta.add(decisaoPropostaPautaNova);
        }

        for (DecisaoPropostaPauta propostasAprovadasWorkflow : decisaoPropostaPautaService.createDecisaoPropostaWorkflow(propostaService.getPropostasAprovadasWorkflow())) {
            decisoesPauta.add(propostasAprovadasWorkflow);
        }

        pauta.setPropostasPauta(decisoesPauta);

        PDFUtil pdfUtil = new PDFUtil();
        ArquivoPauta arquivoPauta = null;

        try {
            arquivoPauta = pdfUtil.criarPDFPauta(pauta, analistaTIresponsavel);
        } catch (Exception e) {
            System.out.println(e);
        }

        pauta.getArquivosPauta().add(arquivoPauta);

        Pauta pautaSalva = pautaService.save(pauta);

        if (!pautaDTO.isTeste()) {
            for (DecisaoPropostaPauta decisaoPropostaPauta : pautaSalva.getPropostasPauta()) {
//            encerrar historico de informar o parecer
                Demanda demandaDecisao = propostaService.findById(decisaoPropostaPauta.getProposta().getIdProposta()).get().getDemanda();
                historicoWorkflowService.finishHistoricoByDemanda(demandaDecisao, Tarefa.INFORMARPARECERFORUM, analistaTIresponsavel, null, null);

//            iniciar histórico de criar uma ata
                historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARATA, StatusHistorico.EMANDAMENTO, analistaTIresponsavel, demandaDecisao);
            }
        }

        Notificacao notificacao = new Notificacao();
        notificacao.setAcao(AcaoNotificacao.REUNIAO);
        notificacao.setDescricaoNotificacao("A comissão adicionou o parecer na pauta. Veja o que foi concluído!");
        notificacao.setTituloNotificacao("Parecer da comissão adicionado");
        notificacao.setTipoNotificacao(TipoNotificacao.PAUTA);
        notificacao.setLinkNotificacao("http://localhost:8081/home/agenda");
        notificacao.setIdComponenteLink(pautaSalva.getIdPauta());

        List<Usuario> usuarios = new ArrayList<>();

        for (DecisaoPropostaPauta decisaoPropostaPauta : pautaSalva.getPropostasPauta()) {
            Usuario gerenteNegocio = usuarioService.findGerenteByDepartamento(decisaoPropostaPauta.getProposta().
                    getDemanda().getUsuario().getDepartamento());

            usuarios.add(gerenteNegocio);
        }

        Usuario analistaTI = usuarioService.findById(idAnalista).get();

        usuarios.add(analistaTI);

        notificacao.setUsuariosNotificacao(usuarios);

        notificacaoService.save(notificacao);

        for (DecisaoPropostaPauta decisaoPropostaPauta : pautaSalva.getPropostasPauta()) {
            simpMessagingTemplate.convertAndSend("/notificacao/demanda/" +
                    decisaoPropostaPauta.getProposta().getDemanda().getIdDemanda(), notificacao);
        }

        return ResponseEntity.status(HttpStatus.OK).body(pauta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }
        pautaService.deleteById(idPauta);
        return ResponseEntity.status(HttpStatus.OK).body("Pauta deletada com sucesso!");
    }

    private boolean validacaoPropostasCriacao(List<Proposta> propostasPauta) {
        //ver se as propostas estão em algum processo de aprovação em aberto
        if (!propostaService.propostasExistem(propostasPauta)) {
            return false;
        }

        //ver se as propostas existem
        if (!propostasLivres(propostasPauta)) {
            return false;
        }

        return true;
    }

    private boolean propostasLivres(List<Proposta> propostasPauta) {
        for (Proposta proposta : propostasPauta) {
            if (proposta.getEstaEmPauta()) {
                return false;
            }
        }

        return true;
    }

    private boolean dataFutura(Date dataReuniao) {
        return dataReuniao.getTime() > new Date().getTime();
    }
}
