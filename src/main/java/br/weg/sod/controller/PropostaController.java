package br.weg.sod.controller;

import br.weg.sod.dto.PropostaDTO;
import br.weg.sod.model.entities.Proposta;
import br.weg.sod.model.service.PropostaService;
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
@RequestMapping("/sod/proposta")
public class PropostaController {

    private PropostaService propostaService;

    @GetMapping
    public ResponseEntity<List<Proposta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(propostaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idProposta) {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(propostaService.findById(idProposta));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PropostaDTO propostaDTO) {
        Proposta proposta = new Proposta();
        BeanUtils.copyProperties(propostaDTO, proposta);

        return ResponseEntity.status(HttpStatus.OK).body(propostaService.save(proposta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid PropostaDTO propostaDTO, @PathVariable(name = "id") Integer idProposta) {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }

        Proposta proposta = propostaService.findById(idProposta).get();
        BeanUtils.copyProperties(propostaDTO, proposta);
        proposta.setIdProposta(idProposta);

        return ResponseEntity.status(HttpStatus.OK).body(propostaService.save(proposta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idProposta) {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }
        propostaService.deleteById(idProposta);
        return ResponseEntity.status(HttpStatus.OK).body("Proposta deletada com sucesso!");
    }
}
