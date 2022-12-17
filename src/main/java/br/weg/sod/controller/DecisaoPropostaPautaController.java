package br.weg.sod.controller;

import br.weg.sod.dto.DecisaoPropostaPautaCriacaoDTO;
import br.weg.sod.dto.DecisaoPropostaPautaEdicaoDTO;
import br.weg.sod.model.entities.AnalistaTI;
import br.weg.sod.model.entities.DecisaoPropostaPauta;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.DecisaoPropostaPautaService;
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
@RequestMapping("/sod/decisaoPropostaPauta")
public class DecisaoPropostaPautaController {

    private DecisaoPropostaPautaService decisaoPropostaPautaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<DecisaoPropostaPauta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaPautaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idDecisaoPropostaPauta) {
        if (!decisaoPropostaPautaService.existsById(idDecisaoPropostaPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaPautaService.findById(idDecisaoPropostaPauta));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DecisaoPropostaPautaCriacaoDTO decisaoPropostaPautaCriacaoDTO) {
        DecisaoPropostaPauta decisaoPropostaPauta = new DecisaoPropostaPauta();
        BeanUtils.copyProperties(decisaoPropostaPautaCriacaoDTO, decisaoPropostaPauta);

        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaPautaService.save(decisaoPropostaPauta));
    }

    @PutMapping("/{idDecisaoProposta}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestBody @Valid DecisaoPropostaPautaEdicaoDTO decisaoPropostaPautaEdicaoDTO, @PathVariable(name = "idDecisaoProspota") Integer idDecisaoPropostaPauta, @PathVariable(name = "idAnalista") Integer idAnalista) {
        if (!decisaoPropostaPautaService.existsById(idDecisaoPropostaPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }

        DecisaoPropostaPauta decisaoPropostaPauta = decisaoPropostaPautaService.findById(idDecisaoPropostaPauta).get();
        BeanUtils.copyProperties(decisaoPropostaPautaEdicaoDTO, decisaoPropostaPauta);

        // Encerrando historico infomar parecer comissao
        historicoWorkflowService.finishHistoricoByProposta(decisaoPropostaPauta.getProposta(), Tarefa.INFORMARPARECERFORUM);

        // iniciando historico informar parecer DG
        AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();

        historicoWorkflowService.initializeHistoricoByProposta(new Timestamp(new Date().getTime()), Tarefa.INFORMARPARECERDG, StatusHistorico.EMANDAMENTO, analistaResponsavel, decisaoPropostaPauta.getProposta());

        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaPautaService.save(decisaoPropostaPauta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idDecisaoPropostaPauta) {
        if (!decisaoPropostaPautaService.existsById(idDecisaoPropostaPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }
        decisaoPropostaPautaService.deleteById(idDecisaoPropostaPauta);
        return ResponseEntity.status(HttpStatus.OK).body("Decisao proposta deletado com sucesso!");
    }
}
