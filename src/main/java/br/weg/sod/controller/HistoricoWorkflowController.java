package br.weg.sod.controller;

import br.weg.sod.dto.HistoricoWorkflowCriacaoDTO;
import br.weg.sod.dto.HistoricoWorkflowEdicaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.*;
import br.weg.sod.model.service.*;
import br.weg.sod.util.HistoricoWorkflowUtil;
import br.weg.sod.util.UtilFunctions;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sod/historicoWorkflow")
public class HistoricoWorkflowController {

    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;
    private DemandaService demandaService;
    private PropostaService propostaService;
    private NotificacaoService notificacaoService;
    private SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping
    public ResponseEntity<List<HistoricoWorkflow>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idHistoricoWorkflow) {
        if (!historicoWorkflowService.existsById(idHistoricoWorkflow)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findById(idHistoricoWorkflow));
    }

    @GetMapping("/demanda/{id}")
    public ResponseEntity<Object> findByDemanda(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findHistoricoWorkflowByDemanda(demandaService.findById(idDemanda).get()));
    }

    @GetMapping("/demanda/ultimo/{id}")
    public ResponseEntity<Object> findLastHistoricoByDemanda(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findLastHistoricoByDemanda(demandaService.findById(idDemanda).get()));
    }

    @GetMapping("/arquivo/{idDemanda}")
    public ResponseEntity<Object> findLastArquivoHistorico(@PathVariable(name = "idDemanda") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findArquivoLastHistoricoByDemanda(demandaService.findById(idDemanda).get()));
    }

    @GetMapping("/aprovadaGerente/{id}")
    public ResponseEntity<Object> demandaIsAprovadaByGerente(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        Demanda demandaHistorico = demandaService.findById(idDemanda).get();
        List<HistoricoWorkflow> historicosDemanda = historicoWorkflowService.findHistoricoWorkflowByDemanda(demandaHistorico);

        for (HistoricoWorkflow historicoWorkflow : historicosDemanda) {
            if (historicoWorkflow.getUsuario() == null) {
                continue;
            }

            Usuario usuarioResponsavel = usuarioService.findById(historicoWorkflow.getUsuario().getIdUsuario()).get();

            if (usuarioResponsavel instanceof GerenteNegocio && historicoWorkflow.getTarefa() == Tarefa.AVALIARDEMANDA && historicoWorkflow.getAcaoFeita() == Tarefa.APROVARDEMANDA) {
                return ResponseEntity.status(HttpStatus.OK).body(true);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    @GetMapping("/analistaResponsavel/{idDemanda}")
    public ResponseEntity<Object> findAnalistaTIResponsavelByDemanda(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findLastHistoricoAnalistaByDemanda(demandaService.findById(idDemanda).get()));
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Object> save(
            @RequestParam("historico") @Valid String historicoJSON,
            @PathVariable(name = "idUsuario") Integer idUsuario)
            throws IOException {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de usuário não encontrado");
        }
        Usuario usuarioResponsavel = usuarioService.findById(idUsuario).get();

        HistoricoWorkflowUtil util = new HistoricoWorkflowUtil();
        HistoricoWorkflowCriacaoDTO historicoWorkflowDTO = util.convertJsontoDtoCriacao(historicoJSON);
        ResponseEntity<Object> historicoValido;

        if (historicoWorkflowDTO.getUsuario() != null) {
            Usuario usuarioProximoHistorico = usuarioService.findById(historicoWorkflowDTO.getUsuario().getIdUsuario()).get();
            System.out.println(usuarioProximoHistorico);
            System.out.println(usuarioProximoHistorico instanceof GerenteNegocio);
            historicoValido = historicoWorkflowService.validaCriacaoHistorico(historicoWorkflowDTO, usuarioProximoHistorico);
        } else {
            historicoValido = historicoWorkflowService.validaCriacaoHistorico(historicoWorkflowDTO, null);
        }

        if (historicoValido != null) {
            return historicoValido;
        }

        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow();
        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow);
        historicoWorkflow.setStatus(StatusHistorico.EMANDAMENTO);

        Demanda demandaRelacionada = demandaService.findById(historicoWorkflowDTO.getDemanda().getIdDemanda()).get();

        historicoWorkflowService.finishHistoricoByDemanda(
                demandaRelacionada,
                historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior(),
                usuarioResponsavel,
                historicoWorkflowDTO.getMotivoDevolucaoAnterior(),
                null
        );

        if(historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior().equals(Tarefa.DEVOLVERDEMANDA)){
            demandaRelacionada.setDevolvida(true);
            demandaService.save(demandaRelacionada);
        }

        criarNotificacao(historicoWorkflowService.findLastHistoricoByDemanda(demandaRelacionada));

        HistoricoWorkflow historicoSalvo = historicoWorkflowService.save(historicoWorkflow);

        if (historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior() == Tarefa.REPROVARWORKFLOW) {
            Proposta propostaAlterada = propostaService.findById(historicoSalvo.getDemanda().getIdDemanda()).get();
            propostaAlterada.setEmWorkflow(false);
            propostaService.save(propostaAlterada);
        }

        return ResponseEntity.status(HttpStatus.OK).body(historicoSalvo);
    }

    private void criarNotificacao(HistoricoWorkflow historico) {
        Notificacao notificacao = new Notificacao();
        switch (historico.getAcaoFeita()){
            case DEVOLVERDEMANDA -> {
                List<Usuario> usuarios = new ArrayList<>();
                usuarios.add(historico.getDemanda().getUsuario());
                notificacao.setAcao(AcaoNotificacao.STATUSDEMANDA);
                notificacao.setDescricaoNotificacao("Demanda devolvida pelo analista de TI");
                notificacao.setTituloNotificacao("Demanda Devolvida");
                notificacao.setTipoNotificacao(TipoNotificacao.DEMANDA);
                notificacao.setLinkNotificacao("http://localhost:8081/home/demand");
                notificacao.setIdComponenteLink(historico.getDemanda().getIdDemanda());
                notificacao.setUsuariosNotificacao(usuarios);
                notificacao = notificacaoService.save(notificacao);
                simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + historico.getDemanda().getIdDemanda(), notificacao);
            }
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestParam("historico") @Valid String historicoJSON, @RequestParam(value = "pdf", required = false) MultipartFile versaoPDF, @PathVariable(name = "id") Integer idHistoricoWorkflow) throws IOException {
        if (!historicoWorkflowService.existsById(idHistoricoWorkflow)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        HistoricoWorkflowUtil util = new HistoricoWorkflowUtil();
        HistoricoWorkflowEdicaoDTO historicoWorkflowDTO = util.convertJsontoDtoEdicao(historicoJSON);
        HistoricoWorkflow historicoWorkflow = historicoWorkflowService.findById(idHistoricoWorkflow).get();

        ResponseEntity<Object> historicoValido = historicoWorkflowService.validaEdicaoHistorico(historicoWorkflowDTO, historicoWorkflow);

        if (historicoValido != null) {
            return historicoValido;
        }

        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow, UtilFunctions.getPropriedadesNulas(historicoWorkflowDTO));

        historicoWorkflow.setIdHistoricoWorkflow(idHistoricoWorkflow);

        if (versaoPDF != null) {
            historicoWorkflow.setArquivoHistoricoWorkflow(new ArquivoHistoricoWorkflow(versaoPDF));
        }

        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowSalvo);
    }

    @PutMapping("/demanda/{idDemanda}")
    public ResponseEntity<Object> editLastByDemanda(
            @RequestParam("historico") @Valid String historicoJSON,
            @RequestParam(value = "pdf", required = false) MultipartFile versaoPDF,
            @PathVariable(name = "idDemanda") Integer idDemanda)
            throws IOException {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        HistoricoWorkflowUtil util = new HistoricoWorkflowUtil();
        HistoricoWorkflowEdicaoDTO historicoWorkflowDTO = util.convertJsontoDtoEdicao(historicoJSON);
        HistoricoWorkflow historicoWorkflow = historicoWorkflowService.findLastHistoricoByDemanda(demandaService.findById(idDemanda).get());

        ResponseEntity<Object> historicoValido = historicoWorkflowService.validaEdicaoHistorico(historicoWorkflowDTO, historicoWorkflow);

        if (historicoValido != null) {
            return historicoValido;
        }

        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow, UtilFunctions.getPropriedadesNulas(historicoWorkflowDTO));

        if (versaoPDF != null) {
            historicoWorkflow.setArquivoHistoricoWorkflow(new ArquivoHistoricoWorkflow(versaoPDF));
        }

        if (historicoWorkflowDTO.getAcaoFeita() == Tarefa.REPROVARDEMANDA) {
            Demanda demandaAtualizar = demandaService.findById(historicoWorkflowDTO.getDemanda().getIdDemanda()).get();
            demandaAtualizar.setStatusDemanda(StatusDemanda.CANCELED);
            demandaService.save(demandaAtualizar);

            Notificacao notificacao = new Notificacao();

            List<Usuario> usuarios = new ArrayList<>();

            usuarios.add(demandaAtualizar.getUsuario());

            notificacao.setAcao(AcaoNotificacao.STATUSDEMANDA);
            notificacao.setTituloNotificacao("Demanda Reprovada");
            notificacao.setDescricaoNotificacao("Demanda reprovada pelo analista de TI");
            notificacao.setTipoNotificacao(TipoNotificacao.DEMANDA);
            notificacao.setLinkNotificacao("http://localhost:8081/home/demand");
            notificacao.setIdComponenteLink(demandaAtualizar.getIdDemanda());
            notificacao.setUsuariosNotificacao(usuarios);

            notificacao = notificacaoService.save(notificacao);

            simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + idDemanda, notificacao);
        }

        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idHistoricoWorkflow) {
        if (!historicoWorkflowService.existsById(idHistoricoWorkflow)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }
        historicoWorkflowService.deleteById(idHistoricoWorkflow);
        return ResponseEntity.status(HttpStatus.OK).body("Historico Workflow deletado com sucesso!");
    }

}
