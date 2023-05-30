package br.weg.sade.controller;

import br.weg.sade.dto.BUDTO;
import br.weg.sade.model.entities.BU;
import br.weg.sade.model.service.BUService;
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
@RequestMapping("/sade/bu")
public class BUController {

    private BUService buService;

    @GetMapping
    public ResponseEntity<List<BU>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(buService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idBU) {
        if (!buService.existsById(idBU)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma BU com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(buService.findById(idBU));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BUDTO budto) {
        BU bu = new BU();
        BeanUtils.copyProperties(budto, bu);
        return ResponseEntity.status(HttpStatus.OK).body(buService.save(bu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid BUDTO budto, @PathVariable(name = "id") Integer idBU) {
        if (!buService.existsById(idBU)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma BU com o ID informado");
        }

        BU bu = buService.findById(idBU).get();
        BeanUtils.copyProperties(budto, bu);
        bu.setIdBU(idBU);

        return ResponseEntity.status(HttpStatus.OK).body(buService.save(bu));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idBU) {
        if (!buService.existsById(idBU)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma BU com o ID informado");
        }
        buService.deleteById(idBU);
        return ResponseEntity.status(HttpStatus.OK).body("BU deletado com sucesso!");
    }
}
