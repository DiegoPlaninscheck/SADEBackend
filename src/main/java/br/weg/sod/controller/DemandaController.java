package br.weg.sod.controller;

import br.weg.sod.dto.DemandaDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.DemandaService;
import br.weg.sod.model.service.HistoricoWorkflowService;
import br.weg.sod.model.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/sod/demanda")
public class DemandaController {

    private DemandaService demandaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Demanda>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findById(idDemanda));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DemandaDTO demandaDTO) {
        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(demandaDTO, demanda);

        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow(Tarefa.AVALIARDEMANDA, StatusHistorico.EMAGUARDO, demanda);
        historicoWorkflowService.save(historicoWorkflow);

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda));
    }

    @PutMapping("/{idDemanda}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestBody @Valid DemandaDTO demandaDTO, @PathVariable(name = "idDemanda") Integer idDemanda, @PathVariable(name = "idAnalista") Integer idAnalista) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        Demanda demanda = demandaService.findById(idDemanda).get();
        BeanUtils.copyProperties(demandaDTO, demanda);
        demanda.setIdDemanda(idDemanda);

        if (demanda.getTamanho() == null) {
            //concluindo histórico da classificacao do analista de TI
            HistoricoWorkflow historicoWorkflowVelho = historicoWorkflowService.findLastHistoricoByDemanda(demanda);
            historicoWorkflowVelho.setConclusaoTarefa(new Timestamp(1));
            historicoWorkflowVelho.setStatus(StatusHistorico.CONCLUIDO);
            historicoWorkflowVelho.setAcaoFeita(Tarefa.CLASSIFICAR);
            historicoWorkflowService.save(historicoWorkflowVelho);
            //dar um jeito no pdf

            //iniciando o histórico de avaliacao do gerente de negócio
            GerenteNegocio gerenteNegocio = usuarioService.findGerenteBySolicitante(demanda.getUsuario().getDepartamento());
            HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow(new Timestamp(1), new Timestamp(5), Tarefa.AVALIARDEMANDA, StatusHistorico.EMANDAMENTO, gerenteNegocio, demanda);
            historicoWorkflowService.save(historicoWorkflow);
        } else {
            //conclui o histórico de adicionar informações
            HistoricoWorkflow historicoWorkflowVelho = historicoWorkflowService.findLastHistoricoByDemanda(demanda);
            historicoWorkflowVelho.setAcaoFeita(Tarefa.ADICIONARINFORMACOES);
            historicoWorkflowVelho.setConclusaoTarefa(new Timestamp(24));
            historicoWorkflowVelho.setStatus(StatusHistorico.CONCLUIDO);
            historicoWorkflowService.save(historicoWorkflowVelho);
            //dar um jeito no pdf

            //inicia o histórico de criar proposta
            AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
            HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow(new Timestamp(2343), new Timestamp(5), Tarefa.CRIARPROPOSTA, StatusHistorico.EMANDAMENTO, analistaResponsavel, demanda);
            historicoWorkflowService.save(historicoWorkflow);
        }

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }
        demandaService.deleteById(idDemanda);
        return ResponseEntity.status(HttpStatus.OK).body("Demanda deletada com sucesso!");
    }
}
