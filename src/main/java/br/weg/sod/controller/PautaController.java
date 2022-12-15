package br.weg.sod.controller;

import br.weg.sod.dto.PautaDTO;
import br.weg.sod.model.entities.AnalistaTI;
import br.weg.sod.model.entities.DecisaoProposta;
import br.weg.sod.model.entities.Pauta;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.HistoricoWorkflowService;
import br.weg.sod.model.service.PautaService;
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
@RequestMapping("/sod/pauta")
public class PautaController {

    private PautaService pautaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Pauta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findById(idPauta));
    }

    @PostMapping("/{idAnalista}")
    public ResponseEntity<Object> save(@RequestBody @Valid PautaDTO pautaDTO, @PathVariable(name = "idAnalista") Integer idAnalista) {
        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(pautaDTO, pauta);
        Pauta pautaSalva = pautaService.save(pauta);

//        for(DecisaoProposta decisaoProposta : pautaSalva.getPropostasPauta()){
            //encerrar historico criar pauta
//            historicoWorkflowService.finishHistoricoByProposta(decisaoProposta.getProposta(), Tarefa.CRIARPAUTA);
//
//            //inicio informar parecer da comissao
//            Timestamp time = new Timestamp(pautaSalva.getDataReuniao().getTime());
//            AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
//
//            historicoWorkflowService.initializeHistoricoByProposta(time,Tarefa.INFORMARPARECERFORUM, StatusHistorico.EMAGUARDO, analistaResponsavel, decisaoProposta.getProposta());
//        }

        return ResponseEntity.status(HttpStatus.OK).body(pautaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid PautaDTO pautaDTO, @PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }

        Pauta pauta = pautaService.findById(idPauta).get();
        BeanUtils.copyProperties(pautaDTO, pauta);
        pauta.setIdPauta(idPauta);

        return ResponseEntity.status(HttpStatus.OK).body(pautaService.save(pauta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }
        pautaService.deleteById(idPauta);
        return ResponseEntity.status(HttpStatus.OK).body("Pauta deletada com sucesso!");
    }
}
