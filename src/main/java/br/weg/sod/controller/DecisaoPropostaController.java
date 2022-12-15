package br.weg.sod.controller;

import br.weg.sod.dto.DecisaoPropostaCriacaoDTO;
import br.weg.sod.dto.DecisaoPropostaEdicaoDTO;
import br.weg.sod.model.entities.AnalistaTI;
import br.weg.sod.model.entities.DecisaoProposta;
import br.weg.sod.model.entities.HistoricoWorkflow;
import br.weg.sod.model.entities.Pauta;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.DecisaoPropostaService;
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
@RequestMapping("/sod/decisaoProposta")
public class DecisaoPropostaController {

    private DecisaoPropostaService decisaoPropostaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<DecisaoProposta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idDecisaoProposta) {
        if (!decisaoPropostaService.existsById(idDecisaoProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaService.findById(idDecisaoProposta));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DecisaoPropostaCriacaoDTO decisaoPropostaCriacaoDTO) {
        DecisaoProposta decisaoProposta = new DecisaoProposta();
        BeanUtils.copyProperties(decisaoPropostaCriacaoDTO, decisaoProposta);

        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaService.save(decisaoProposta));
    }

    @PutMapping("/{idDecisaoProposta}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestBody @Valid DecisaoPropostaEdicaoDTO decisaoPropostaDTO, @PathVariable(name = "idDecisaoProspota") Integer idDecisaoProposta, @PathVariable(name = "idAnalista") Integer idAnalista) {
        if (!decisaoPropostaService.existsById(idDecisaoProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }

        DecisaoProposta decisaoProposta = decisaoPropostaService.findById(idDecisaoProposta).get();
        BeanUtils.copyProperties(decisaoPropostaDTO, decisaoProposta);

        // Encerrando historico infomar parecer comissao
        historicoWorkflowService.finishHistoricoByProposta(decisaoProposta.getProposta(), Tarefa.INFORMARPARECERFORUM);

        // iniciando historico informar parecer DG
        AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();

        historicoWorkflowService.initializeHistoricoByProposta(new Timestamp(new Date().getTime()), Tarefa.INFORMARPARECERDG, StatusHistorico.EMANDAMENTO, analistaResponsavel, decisaoProposta.getProposta());

        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaService.save(decisaoProposta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idDecisaoProposta) {
        if (!decisaoPropostaService.existsById(idDecisaoProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }
        decisaoPropostaService.deleteById(idDecisaoProposta);
        return ResponseEntity.status(HttpStatus.OK).body("Decisao proposta deletado com sucesso!");
    }
}
