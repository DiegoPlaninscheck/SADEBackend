package br.weg.sod.controller;

import br.weg.sod.dto.HistoricoWorkflowCriacaoDTO;
import br.weg.sod.dto.HistoricoWorkflowEdicaoDTO;
import br.weg.sod.dto.NotificacaoDTO;
import br.weg.sod.dto.NotificacaoUsuarioDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.entities.enuns.TipoNotificacao;
import br.weg.sod.model.service.HistoricoWorkflowService;
import br.weg.sod.model.service.NotificacaoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/sod/historicoWorkflow")
public class HistoricoWorkflowController {

    private HistoricoWorkflowService historicoWorkflowService;
    private NotificacaoController notificacaoController;

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

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid HistoricoWorkflowCriacaoDTO historicoWorkflowCriacaoDTO) {
        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow();
        BeanUtils.copyProperties(historicoWorkflowCriacaoDTO, historicoWorkflow);
        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.save(historicoWorkflow));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid HistoricoWorkflowEdicaoDTO historicoWorkflowCriacaoDTO, @PathVariable(name = "id") Integer idHistoricoWorkflow) {
        if (!historicoWorkflowService.existsById(idHistoricoWorkflow)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        HistoricoWorkflow historicoWorkflow = historicoWorkflowService.findById(idHistoricoWorkflow).get();
        BeanUtils.copyProperties(historicoWorkflowCriacaoDTO, historicoWorkflow);
        historicoWorkflow.setIdHistoricoWorkflow(idHistoricoWorkflow);
        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);

        Tarefa acaoFeita = historicoWorkflowSalvo.getAcaoFeita();

        if (acaoFeita != null) {
            String tituloNotificacao = "";
            String linkNotificacao = "http://localhost:8081/";
            String descricaoNotificação = "";
            TipoNotificacao tipoNotificacao = null;
            Integer idComponente = null;
            List<Usuario> usuarioRelacionados = new ArrayList<>();

            Tarefa tarefa = historicoWorkflowSalvo.getTarefa();

            if (tarefa == Tarefa.AVALIARDEMANDA) {
                Usuario responsavelTarefa = historicoWorkflowSalvo.getUsuario();
                String tituloDemanda = historicoWorkflowSalvo.getDemanda().getTituloDemanda();
                linkNotificacao += "/notifications/demand";
                tipoNotificacao = TipoNotificacao.DEMANDA;
                idComponente = historicoWorkflow.getDemanda().getIdDemanda();

                if (responsavelTarefa instanceof AnalistaTI) {
                    tituloNotificacao = "Avaliação da demanda concluída";
                    descricaoNotificação = "Sua demanda " + tituloDemanda;

                    if (acaoFeita == Tarefa.APROVAR) {
                        descricaoNotificação += " aprovada, parabéns!";
                    } else if (acaoFeita == Tarefa.REPROVAR) {
                        descricaoNotificação += " reprovada.";
                    } else if (acaoFeita == Tarefa.DEVOLVER) {
                        descricaoNotificação += " devolvida, reveja e elabore ela conforme o indicado";
                    }

                    usuarioRelacionados.add(historicoWorkflow.getDemanda().getUsuario());
                } else if (responsavelTarefa instanceof GerenteNegocio) {

                    if (acaoFeita == Tarefa.APROVAR) {
                        tituloNotificacao = "Demanda aprovada";
                        descricaoNotificação = "A demanda " + tituloDemanda + " foi aprovada pelo gerente de negócio do solicitante!";

                        List<HistoricoWorkflow> historicosDemanda = historicoWorkflowService.findByDemanda(historicoWorkflowSalvo.getDemanda());
                        HistoricoWorkflow historicoAnterior = historicosDemanda.get(historicosDemanda.size() - 2);
                        AnalistaTI analistaResponsavel = (AnalistaTI) historicoAnterior.getUsuario();

                        usuarioRelacionados.add(analistaResponsavel);
                    } else if (acaoFeita == Tarefa.REPROVAR) {
                        tituloNotificacao = "Demanda reprovada";
                        descricaoNotificação = "Sua demanda" + tituloDemanda + " foi reprovada";

                        usuarioRelacionados.add(historicoWorkflow.getDemanda().getUsuario());
                    }
                }
            } else if (tarefa == Tarefa.CRIARPROPOSTA) {
                Usuario responsavelTarefa = historicoWorkflowSalvo.getUsuario();
                String tituloProposta = historicoWorkflowSalvo.getDemanda().getTituloDemanda();
                linkNotificacao += "/notifications/proposal";
                tipoNotificacao = TipoNotificacao.PROPOSTA;
                idComponente = historicoWorkflow.getDemanda().getIdDemanda();



            }

            notificacaoController.save(new NotificacaoDTO(tituloNotificacao, descricaoNotificação, linkNotificacao, tipoNotificacao, idComponente, usuarioRelacionados));
        }

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
