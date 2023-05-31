package br.weg.sade.controller;

import br.weg.sade.model.dto.DecisaoPropostaATADTO;
import br.weg.sade.model.entity.DecisaoPropostaATA;
import br.weg.sade.service.DecisaoPropostaATAService;
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
@RequestMapping("/sade/decisaoPropostaAta")
public class DecisaoPropostaATAController {

    private DecisaoPropostaATAService decisaoPropostaATAService;

    @GetMapping
    public ResponseEntity<List<DecisaoPropostaATA>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaATAService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idDecisaoPropostaATA) {
        if (!decisaoPropostaATAService.existsById(idDecisaoPropostaATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaATAService.findById(idDecisaoPropostaATA));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DecisaoPropostaATADTO decisaoPropostaATADTO) {
        DecisaoPropostaATA decisaoPropostaATA = new DecisaoPropostaATA();
        BeanUtils.copyProperties(decisaoPropostaATADTO, decisaoPropostaATA);

        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaATAService.save(decisaoPropostaATA));
    }

    @PutMapping("/{idDecisaoProposta}")
    public ResponseEntity<Object> edit(@RequestBody @Valid DecisaoPropostaATADTO decisaoPropostaATADTO, @PathVariable(name = "idDecisaoProspota") Integer idDecisaoPropostaATA) {
        if (!decisaoPropostaATAService.existsById(idDecisaoPropostaATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }

        DecisaoPropostaATA decisaoPropostaAta = decisaoPropostaATAService.findById(idDecisaoPropostaATA).get();
        BeanUtils.copyProperties(decisaoPropostaATADTO, decisaoPropostaAta);

        return ResponseEntity.status(HttpStatus.OK).body(decisaoPropostaATAService.save(decisaoPropostaAta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idDecisaoPropostaATA) {
        if (!decisaoPropostaATAService.existsById(idDecisaoPropostaATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma decisao proposta com o ID informado");
        }

        decisaoPropostaATAService.deleteById(idDecisaoPropostaATA);
        return ResponseEntity.status(HttpStatus.OK).body("Decisao proposta deletado com sucesso!");
    }
}
