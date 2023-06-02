package br.weg.sade.controller;

import br.weg.sade.model.dto.ATACriacaoDTO;
import br.weg.sade.model.dto.ATAEdicaoDTO;
import br.weg.sade.model.dto.DecisaoPropostaATADTO;
import br.weg.sade.model.entity.*;
import br.weg.sade.model.enums.*;
import br.weg.sade.service.*;
import br.weg.sade.util.ATAUtil;
import br.weg.sade.util.PDFUtil;
import br.weg.sade.util.UtilFunctions;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sade/ata")
public class ATAController {

    private ATAService ataService;
    private HistoricoWorkflowService historicoWorkflowService;
    private DecisaoPropostaATAService decisaoPropostaATAService;
    private UsuarioService usuarioService;
    private PautaService pautaService;
    private DemandaService demandaService;

    private NotificacaoService notificacaoService;

    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public ResponseEntity<List<ATA>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(ataService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idATA) {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com este ID");
        }

        return ResponseEntity.status(HttpStatus.OK).body(ataService.findById(idATA));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ATACriacaoDTO ataDTO) throws IOException {
        if (!pautaService.existsById(ataDTO.getPauta().getIdPauta())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de pauta informado inválido");
        }

        if (ataDTO.getFinalReuniao().getTime() < ataDTO.getInicioReuniao().getTime()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Horários de reunião inválidos");
        }

        ATA ata = new ATA();
        BeanUtils.copyProperties(ataDTO, ata);
        ata.setUsuariosReuniaoATA(usuarioService.findGerentesTI());

        ata.setPropostasAta(atualizarPauta(ata, null));

        ATA ataSalva = ataService.save(ata);

        Notificacao notificacao = new Notificacao();
        notificacao.setAcao(AcaoNotificacao.VIROUATA);
        notificacao.setDescricaoNotificacao("A sua pauta acabou de virar um ATA");
        notificacao.setTituloNotificacao("Nova ATA criada");
        notificacao.setTipoNotificacao(TipoNotificacao.ATA);
        notificacao.setLinkNotificacao("http://localhost:8081/home/ata");
        notificacao.setIdComponenteLink(ataSalva.getIdATA());

        List<Usuario> usuarios = new ArrayList<>();

        for(DecisaoPropostaATA decisaoPropostaATA : ataSalva.getPropostasAta()){
            Usuario gerenteNegocio = usuarioService.findGerenteByDepartamento(decisaoPropostaATA.getProposta().
                    getDemanda().getUsuario().getDepartamento());

            usuarios.add(decisaoPropostaATA.getProposta().getDemanda().getUsuario());
            usuarios.add(gerenteNegocio);
        }

        notificacao.setUsuariosNotificacao(usuarios);

        notificacaoService.save(notificacao);

        for (DecisaoPropostaATA decisaoPropostaATA : ataSalva.getPropostasAta()) {
            simpMessagingTemplate.convertAndSend("/notificacao/demanda/" +
                    decisaoPropostaATA.getProposta().getDemanda().getIdDemanda(), notificacao);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ataSalva);
    }

    @PostMapping("/{idAnalista}")
    public ResponseEntity<Object> save(
            @RequestParam("ata") @Valid String ataJSON,
            @RequestParam(value = "arquivos", required = false) MultipartFile[] multipartFiles,
            @PathVariable(name = "idAnalista") Integer idAnalista) throws IOException {
        ATACriacaoDTO ataDTO = new ATAUtil().convertJsontoDtoCriacao(ataJSON);

        if (ataDTO.getFinalReuniao().getTime() < ataDTO.getInicioReuniao().getTime()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Horários de reunião inválidos");
        }

        if (!pautaService.existsById(ataDTO.getPauta().getIdPauta())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de pauta informado inválido");
        }

        ATA ata = new ATA();

        BeanUtils.copyProperties(ataDTO, ata);
        ata.setUsuariosReuniaoATA(usuarioService.findGerentesTI());

        Usuario usuarioResponsavel = usuarioService.findById(idAnalista).get();

        ata = processoArquivos(ata, multipartFiles, usuarioResponsavel);

        ata.setPropostasAta(atualizarPauta(ata, usuarioResponsavel));

        ATA ataSalva = ataService.save(ata);

        Notificacao notificacao = new Notificacao();
        notificacao.setAcao(AcaoNotificacao.VIROUATA);
        notificacao.setDescricaoNotificacao("A sua pauta acabou de virar um ATA");
        notificacao.setTituloNotificacao("Nova ATA criada");
        notificacao.setTipoNotificacao(TipoNotificacao.ATA);
        notificacao.setLinkNotificacao("http://localhost:8081/home/ata");
        notificacao.setIdComponenteLink(ataSalva.getIdATA());

        List<Usuario> usuarios = new ArrayList<>();

        for(DecisaoPropostaATA decisaoPropostaATA : ataSalva.getPropostasAta()){
            Usuario gerenteNegocio = usuarioService.findGerenteByDepartamento(decisaoPropostaATA.getProposta().
                            getDemanda().getUsuario().getDepartamento());

            usuarios.add(decisaoPropostaATA.getProposta().getDemanda().getUsuario());
            usuarios.add(gerenteNegocio);
        }

        notificacao.setUsuariosNotificacao(usuarios);

        notificacaoService.save(notificacao);

        for (DecisaoPropostaATA decisaoPropostaATA : ataSalva.getPropostasAta()) {
            simpMessagingTemplate.convertAndSend("/notificacao/demanda/" +
                    decisaoPropostaATA.getProposta().getDemanda().getIdDemanda(), notificacao);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ataSalva);
    }

    @PutMapping("/{idATA}/{idAnalista}")
    public ResponseEntity<Object> edit(
            @RequestParam("ata") @Valid String ataJSON,
            @RequestParam(value = "arquivos", required = false) MultipartFile[] multipartFiles,
            @PathVariable(name = "idATA") Integer idATA,
            @PathVariable(name = "idAnalista") Integer idAnalista)
            throws IOException {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com o ID informado");
        }

        ATAUtil util = new ATAUtil();
        ATA ata = ataService.findById(idATA).get();
        ATAEdicaoDTO ataDTO = util.convertJsontoDto(ataJSON);

        ResponseEntity<Object> validacaoEdicao = validacoesEdicaoATA(ataDTO, ata, multipartFiles);

        if (validacaoEdicao != null) {
            return validacaoEdicao;
        }

        BeanUtils.copyProperties(ataDTO, ata, UtilFunctions.getPropriedadesNulas(ataDTO));
        ata.setIdATA(idATA);
        Usuario analistaTIresponsavel = usuarioService.findById(idAnalista).get();

        ata = processoArquivos(ata, multipartFiles, analistaTIresponsavel);

        for (DecisaoPropostaATADTO decisaoPropostaATADTO : ataDTO.getPropostasAta()) {
            DecisaoPropostaATA decisaoPropostaPauta = new DecisaoPropostaATA();
            BeanUtils.copyProperties(decisaoPropostaATADTO, decisaoPropostaPauta);
            ata.getPropostasAta().add(decisaoPropostaPauta);
        }

        PDFUtil pdfUtil = new PDFUtil();
        ArquivoPauta arquivoPauta = null;

        try {
            arquivoPauta = pdfUtil.criarPDFATA(ata, analistaTIresponsavel);
        }catch (Exception e){
            System.out.println(e);
        }

        ata.getPauta().getArquivosPauta().add(arquivoPauta);

        ATA ataSalva = ataService.save(ata);

        Notificacao notificacao = new Notificacao();
        notificacao.setAcao(AcaoNotificacao.AVALIACAODG);
        notificacao.setDescricaoNotificacao("A ATA foi avaliada pela Direção Geral");
        notificacao.setTituloNotificacao("ATA avaliada pela DG");
        notificacao.setTipoNotificacao(TipoNotificacao.ATA);
        notificacao.setLinkNotificacao("http://localhost:8081/home/ata");
        notificacao.setIdComponenteLink(ataSalva.getIdATA());

        List<Usuario> usuarios = new ArrayList<>();

        for (DecisaoPropostaATA deicasaoProposta : ataSalva.getPropostasAta()) {
            Demanda demandaDecisao = demandaService.findById(deicasaoProposta.getProposta().getIdProposta()).get();
            Tarefa tarefaStatus;
            StatusDemanda statusEscolhidoComissao = deicasaoProposta.getStatusDemandaComissao();
            boolean continuaProcesso = false;

            if (statusEscolhidoComissao == StatusDemanda.TODO) {
                tarefaStatus = Tarefa.FINALIZAR;
            } else if (statusEscolhidoComissao == StatusDemanda.CANCELED) {
                tarefaStatus = Tarefa.REPROVARDEMANDA;
            } else {
                tarefaStatus = Tarefa.CRIARPAUTA;
                continuaProcesso = true;
            }

            demandaDecisao.setStatusDemanda(statusEscolhidoComissao);
            demandaService.save(demandaDecisao);

            //finaliza histórico de informar parecer da DG
            historicoWorkflowService.finishHistoricoByDemanda(demandaDecisao, tarefaStatus, analistaTIresponsavel, null, null);

            if (continuaProcesso) {
                //inicia histórico de criar pauta
                historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARPAUTA, StatusHistorico.EMANDAMENTO, analistaTIresponsavel, demandaDecisao);
            }

            Usuario gerenteNegocio = usuarioService.findGerenteByDepartamento(deicasaoProposta.getProposta().
                    getDemanda().getUsuario().getDepartamento());

            usuarios.add(deicasaoProposta.getProposta().getDemanda().getUsuario());
            usuarios.add(gerenteNegocio);
        }

        notificacao.setUsuariosNotificacao(usuarios);

        notificacaoService.save(notificacao);

        for (DecisaoPropostaATA decisaoPropostaATA : ataSalva.getPropostasAta()) {
            simpMessagingTemplate.convertAndSend("/notificacao/demanda/" +
                    decisaoPropostaATA.getProposta().getDemanda().getIdDemanda(), notificacao);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ataSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idATA) {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com este ID");
        }
        ATA ata = ataService.findById(idATA).get();

        ata.setUsuariosReuniaoATA(null);

        ataService.save(ata);

        ataService.deleteById(idATA);
        return ResponseEntity.status(HttpStatus.OK).body("ATA deletada com sucesso!");
    }

    private ResponseEntity<Object> validacoesEdicaoATA(ATAEdicaoDTO ataDTO, ATA ata, MultipartFile multipartFiles[]) {
        if (ataDTO.getFinalReuniao() != null && ataDTO.getInicioReuniao() != null) {
            if (ataDTO.getFinalReuniao().getTime() < ataDTO.getInicioReuniao().getTime()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Horários de reunião inválidos");
            }
        }

        if (!decisaoPropostaATAService.decisoesValidas(ata.getPauta().getPropostasPauta(), ataDTO.getPropostasAta())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Decisões da ata contém número de id de proposta inválido ou número sequencial já registrado/repetido");
        }

        return null;
    }

    private ATA processoArquivos(ATA ata, MultipartFile[] multipartFiles, Usuario analistaTIresponsavel) throws IOException {
        if (multipartFiles != null) {
            List<ArquivoPauta> arquivosAta = ata.getPauta().getArquivosPauta();

            for (int i = 0; i < multipartFiles.length; i++) {
                arquivosAta.add(new ArquivoPauta(multipartFiles[i], analistaTIresponsavel));
            }

            ata.getPauta().setArquivosPauta(arquivosAta);
        }

        return ata;
    }

    private List<DecisaoPropostaATA> atualizarPauta(ATA ata, Usuario usuarioResponsavel) throws IOException {
        Pauta pautaDaAta = pautaService.findById(ata.getPauta().getIdPauta()).get();
        List<DecisaoPropostaATA> decisoesPropostasATA = new ArrayList<>();

        for (DecisaoPropostaPauta decisaoPropostaPauta : pautaDaAta.getPropostasPauta()) {
            DecisaoPropostaATA decisaoPropostaATA = new DecisaoPropostaATA();
            decisaoPropostaATA.setProposta(decisaoPropostaPauta.getProposta());

            decisoesPropostasATA.add(decisaoPropostaATA);

            Demanda demandaDecisao = decisaoPropostaATA.getProposta().getDemanda();

            historicoWorkflowService.finishHistoricoByDemanda(
                    demandaDecisao,
                    Tarefa.CRIARATA,
                    usuarioResponsavel,
                    null,
                    null
            );

            Timestamp reuniao = new Timestamp(ata.getDataReuniao().getTime());

            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), reuniao, Tarefa.INFORMARPARECERDG, StatusHistorico.EMAGUARDO, usuarioResponsavel, demandaDecisao);
        }

        pautaDaAta.setPertenceUmaATA(true);
        pautaService.save(pautaDaAta);

        return decisoesPropostasATA;
    }

}
