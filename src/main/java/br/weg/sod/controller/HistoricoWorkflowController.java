package br.weg.sod.controller;

import br.weg.sod.dto.HistoricoWorkflowCriacaoDTO;
import br.weg.sod.dto.HistoricoWorkflowEdicaoDTO;
import br.weg.sod.dto.NotificacaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.entities.enuns.TipoNotificacao;
import br.weg.sod.model.service.HistoricoWorkflowService;
import br.weg.sod.model.service.UsuarioService;
import br.weg.sod.util.DemandaUtil;
import br.weg.sod.util.HistoricoWorkflowUtil;
import br.weg.sod.util.UtilFunctions;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@Controller
@RequestMapping("/sod/historicoWorkflow")
public class HistoricoWorkflowController {

    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;

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

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Object> save(@RequestParam("historico") @Valid String historicoJSON, @RequestParam(value = "pdf", required = false) MultipartFile versaoPDF, @PathVariable(name = "idUsuario") Integer idUsuario) throws IOException {
        if(!usuarioService.existsById(idUsuario)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de usuário não encontrado");
        }
        Usuario usuarioResponsavel = usuarioService.findById(idUsuario).get();

        HistoricoWorkflowUtil util = new HistoricoWorkflowUtil();
        HistoricoWorkflowCriacaoDTO historicoWorkflowDTO = util.convertJsontoDtoCriacao(historicoJSON);
        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow();
        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow);
        historicoWorkflow.setStatus(StatusHistorico.EMANDAMENTO);

        if(versaoPDF != null && versaoPDF.isEmpty()){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("PDF da versão não informado");
        }

        //encerra o histórico anterior
        encerrarHistoricoAnterior(historicoWorkflowDTO.getDemanda(), historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior(), usuarioResponsavel, versaoPDF);

        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestParam("historico") @Valid String historicoJSON, @RequestParam(value = "pdf", required = false) MultipartFile versaoPDF, @PathVariable(name = "id") Integer idHistoricoWorkflow) throws IOException {
        if (!historicoWorkflowService.existsById(idHistoricoWorkflow)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        HistoricoWorkflowUtil util = new HistoricoWorkflowUtil();
        HistoricoWorkflowEdicaoDTO historicoWorkflowDTO = util.convertJsontoDtoEdicao(historicoJSON);

        HistoricoWorkflow historicoWorkflow = historicoWorkflowService.findById(idHistoricoWorkflow).get();
        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow, UtilFunctions.getPropriedadesNulas(historicoWorkflowDTO));

        historicoWorkflow.setIdHistoricoWorkflow(idHistoricoWorkflow);

        if(versaoPDF != null){
            historicoWorkflow.setArquivoHistoricoWorkflow(new ArquivoHistoricoWorkflow(versaoPDF));
        }

        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);

//        Tarefa acaoFeita = historicoWorkflowSalvo.getAcaoFeita();
//
//        if (acaoFeita != null) {
//            String tituloNotificacao = "";
//            String linkNotificacao = "http://localhost:8081/";
//            String descricaoNotificação = "";
//            TipoNotificacao tipoNotificacao = null;
//            Integer idComponente = null;
//            List<Usuario> usuarioRelacionados = new ArrayList<>();
//
//            Tarefa tarefa = historicoWorkflowSalvo.getTarefa();
//
//            if (tarefa == Tarefa.AVALIARDEMANDA) {
//                Usuario responsavelTarefa = historicoWorkflowSalvo.getUsuario();
//                String tituloDemanda = historicoWorkflowSalvo.getDemanda().getTituloDemanda();
//                linkNotificacao += "/notifications/demand";
//                tipoNotificacao = TipoNotificacao.DEMANDA;
//                idComponente = historicoWorkflow.getDemanda().getIdDemanda();
//
//                if (responsavelTarefa instanceof AnalistaTI) {
//                    tituloNotificacao = "Avaliação da demanda concluída";
//                    descricaoNotificação = "Sua demanda " + tituloDemanda;
//
//                    if (acaoFeita == Tarefa.APROVAR) {
//                        descricaoNotificação += " aprovada, parabéns!";
//                    } else if (acaoFeita == Tarefa.REPROVAR) {
//                        descricaoNotificação += " reprovada.";
//                    } else if (acaoFeita == Tarefa.DEVOLVER) {
//                        descricaoNotificação += " devolvida, reveja e elabore ela conforme o indicado";
//                    }
//
//                    usuarioRelacionados.add(historicoWorkflow.getDemanda().getUsuario());
//                } else if (responsavelTarefa instanceof GerenteNegocio) {
//
//                    if (acaoFeita == Tarefa.APROVAR) {
//                        tituloNotificacao = "Demanda aprovada";
//                        descricaoNotificação = "A demanda " + tituloDemanda + " foi aprovada pelo gerente de negócio do solicitante!";
//
//                        List<HistoricoWorkflow> historicosDemanda = historicoWorkflowService.findByDemanda(historicoWorkflowSalvo.getDemanda());
//                        HistoricoWorkflow historicoAnterior = historicosDemanda.get(historicosDemanda.size() - 2);
//                        AnalistaTI analistaResponsavel = (AnalistaTI) historicoAnterior.getUsuario();
//
//                        usuarioRelacionados.add(analistaResponsavel);
//                    } else if (acaoFeita == Tarefa.REPROVAR) {
//                        tituloNotificacao = "Demanda reprovada";
//                        descricaoNotificação = "Sua demanda" + tituloDemanda + " foi reprovada";
//
//                        usuarioRelacionados.add(historicoWorkflow.getDemanda().getUsuario());
//                    }
//                }
//            } else if (tarefa == Tarefa.CRIARPROPOSTA) {
//                Usuario responsavelTarefa = historicoWorkflowSalvo.getUsuario();
//                String tituloProposta = historicoWorkflowSalvo.getDemanda().getTituloDemanda();
//                linkNotificacao += "/notifications/proposal";
//                tipoNotificacao = TipoNotificacao.PROPOSTA;
//                idComponente = historicoWorkflow.getDemanda().getIdDemanda();
//
//
//
//            }
//
//            notificacaoController.save(new NotificacaoDTO(tituloNotificacao, descricaoNotificação, linkNotificacao, tipoNotificacao, idComponente, usuarioRelacionados));
//        }

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

    private void encerrarHistoricoAnterior(Demanda demanda, Tarefa acaoFeitaHistoricoAnterior, Usuario usuarioResponsavel, MultipartFile versaoPDF) throws IOException {
        if(versaoPDF == null){
            historicoWorkflowService.finishHistoricoByDemanda(demanda, acaoFeitaHistoricoAnterior,usuarioResponsavel, null);
        } else {
            historicoWorkflowService.finishHistoricoByDemanda(demanda, acaoFeitaHistoricoAnterior, usuarioResponsavel,new ArquivoHistoricoWorkflow(versaoPDF));
        }
    }

}
