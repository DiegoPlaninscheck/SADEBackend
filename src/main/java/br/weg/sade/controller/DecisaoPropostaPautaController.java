package br.weg.sade.controller;

import br.weg.sade.model.dto.DecisaoPropostaPautaCriacaoDTO;
import br.weg.sade.model.dto.DecisaoPropostaPautaEdicaoDTO;
import br.weg.sade.model.entity.DecisaoPropostaPauta;
import br.weg.sade.service.DecisaoPropostaPautaService;
import br.weg.sade.service.HistoricoWorkflowService;
import br.weg.sade.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sade/decisaoPropostaPauta")
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

//        // Encerrando historico infomar parecer comissao
//        historicoWorkflowService.finishHistoricoByProposta(decisaoPropostaPauta.getProposta(), Tarefa.INFORMARPARECERFORUM);
//
//        // iniciando historico informar parecer DG
//        AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
//
//        historicoWorkflowService.initializeHistoricoByProposta(new Timestamp(new Date().getTime()), Tarefa.INFORMARPARECERDG, StatusHistorico.EMANDAMENTO, analistaResponsavel, decisaoPropostaPauta.getProposta());

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
