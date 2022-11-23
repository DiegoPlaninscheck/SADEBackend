package br.weg.sod.controller;

import br.weg.sod.dto.ATADTO;
import br.weg.sod.model.entities.ATA;
import br.weg.sod.model.entities.DecisaoProposta;
import br.weg.sod.model.entities.HistoricoWorkflow;
import br.weg.sod.model.entities.Proposta;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.ATAService;
import br.weg.sod.model.service.DecisaoPropostaService;
import br.weg.sod.model.service.HistoricoWorkflowService;
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
@RequestMapping("/sod/ata")
public class ATAController {

    private ATAService ataService;
    private HistoricoWorkflowService historicoWorkflowService;
    private DecisaoPropostaService decisaoPropostaService;

    @GetMapping
    public ResponseEntity<List<ATA>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(ataService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idATA) {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com este ID");
        }

        return ResponseEntity.status(HttpStatus.OK).body(ataService.findById(idATA));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ATADTO atadto) {
        ATA ata = new ATA();
        BeanUtils.copyProperties(atadto, ata);
        return ResponseEntity.status(HttpStatus.OK).body(ataService.save(ata));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid ATADTO atadto, @PathVariable(name = "id") Integer idATA) {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com o ID informado");
        }

        ATA ata = ataService.findById(idATA).get();
        BeanUtils.copyProperties(atadto, ata);
        ata.setIdATA(idATA);

        List<DecisaoProposta> listaDecisaoProposta = decisaoPropostaService.findByATA(ata);

        for (DecisaoProposta deicasaoProposta : listaDecisaoProposta) {
            Proposta proposta = deicasaoProposta.getProposta();
            historicoWorkflowService.finishHistoricoByProposta(proposta, Tarefa.INFORMARPARECERDG);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ataService.save(ata));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idATA) {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com este ID");
        }
        ataService.deleteById(idATA);
        return ResponseEntity.status(HttpStatus.OK).body("ATA deletada com sucesso!");
    }
}
