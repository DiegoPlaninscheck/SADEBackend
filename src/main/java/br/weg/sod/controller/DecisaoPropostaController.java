package br.weg.sod.controller;

import br.weg.sod.dto.DecisaoPropostaDTO;
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

    @PostMapping("/{idAnalista}")
    public ResponseEntity<Object> save(@RequestBody @Valid DecisaoPropostaDTO decisaoPropostaDTO, @PathVariable(name = "idAnalista") Integer idAnalista) {
        DecisaoProposta decisaoProposta = new DecisaoProposta();
        BeanUtils.copyProperties(decisaoPropostaDTO, decisaoProposta);

        //encerrar histprico criar pauta
        HistoricoWorkflow historicoWorkflowVelho = historicoWorkflowService.findLastHistoricoByProposta(decisaoProposta.getProposta());
        historicoWorkflowVelho.setConclusaoTarefa(new Timestamp(1));
        historicoWorkflowVelho.setStatus(StatusHistorico.CONCLUIDO);
        historicoWorkflowVelho.setAcaoFeita(Tarefa.CRIARPAUTA);


        //inicio informar parecer da comissao
        Pauta pauta = decisaoProposta.getPauta();
        Timestamp time = new Timestamp(pauta.getDataReuniao().getTime());
        AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow(time , new Timestamp(time.getTime() + 5), Tarefa.INFORMARPARECERFORUM, StatusHistorico.EMAGUARDO, analistaResponsavel, decisaoProposta.getProposta().getDemanda() );

        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaService.save(decisaoProposta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid DecisaoPropostaDTO decisaoPropostaDTO, @PathVariable(name = "id") Integer idDecisaoProposta) {
        if (!decisaoPropostaService.existsById(idDecisaoProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }

        DecisaoProposta decisaoProposta = decisaoPropostaService.findById(idDecisaoProposta).get();
        BeanUtils.copyProperties(decisaoPropostaDTO, decisaoProposta);

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
