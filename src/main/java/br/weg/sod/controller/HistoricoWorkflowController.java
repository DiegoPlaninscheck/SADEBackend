package br.weg.sod.controller;

import br.weg.sod.dto.HistoricoWorkflowCriacaoDTO;
import br.weg.sod.dto.HistoricoWorkflowEdicaoDTO;
import br.weg.sod.model.entities.HistoricoWorkflow;
import br.weg.sod.model.service.HistoricoWorkflowService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/sod/historicoWorkflow")
public class HistoricoWorkflowController {

    private HistoricoWorkflowService historicoWorkflowService;

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

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.save(historicoWorkflow));
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
