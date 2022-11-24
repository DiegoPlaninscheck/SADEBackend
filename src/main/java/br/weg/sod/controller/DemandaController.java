package br.weg.sod.controller;

import br.weg.sod.dto.DemandaCriacaoDTO;
import br.weg.sod.dto.DemandaEdicaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
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
    public ResponseEntity<Object> save(@RequestBody @Valid DemandaCriacaoDTO demandaCriacaoDTO) {
        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(demandaCriacaoDTO, demanda);
        demanda.setStatusDemanda(StatusDemanda.BACKLOG);

        Demanda demandaCadastrada = demandaService.save(demanda);

        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow(Tarefa.AVALIARDEMANDA, StatusHistorico.EMAGUARDO, demandaCadastrada);
        historicoWorkflowService.save(historicoWorkflow);

        return ResponseEntity.status(HttpStatus.OK).body(demandaCadastrada);
    }

    @PutMapping("/{idDemanda}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestBody @Valid DemandaEdicaoDTO demandaCriacaoDTO, @PathVariable(name = "idDemanda") Integer idDemanda, @PathVariable(name = "idAnalista") Integer idAnalista) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        Demanda demanda = demandaService.findById(idDemanda).get();
        BeanUtils.copyProperties(demandaCriacaoDTO, demanda);
        demanda.setIdDemanda(idDemanda);

        if (demanda.getTamanho() == null) {
            //concluindo histórico da classificacao do analista de TI
            historicoWorkflowService.finishHistoricoByDemanda(demanda, Tarefa.CLASSIFICAR);

            //iniciando o histórico de avaliacao do gerente de negócio
            GerenteNegocio gerenteNegocio = usuarioService.findGerenteBySolicitante(demanda.getUsuario().getDepartamento());
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.AVALIARDEMANDA, StatusHistorico.EMANDAMENTO, gerenteNegocio, demanda);

        } else {
            //conclui o histórico de adicionar informações
            historicoWorkflowService.finishHistoricoByDemanda(demanda, Tarefa.ADICIONARINFORMACOES);

            //inicia o histórico de criar proposta
            AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARPROPOSTA, StatusHistorico.EMANDAMENTO, analistaResponsavel, demanda);

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
