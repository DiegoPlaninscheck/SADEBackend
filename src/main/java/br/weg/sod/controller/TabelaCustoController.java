package br.weg.sod.controller;

import br.weg.sod.dto.TabelaCustoDTO;
import br.weg.sod.model.entities.TabelaCusto;
import br.weg.sod.model.service.TabelaCustoService;
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
@RestController
@RequestMapping("/sod/tabelaCusto")
public class TabelaCustoController {

    private TabelaCustoService tabelaCustoService;

    @GetMapping
    public ResponseEntity<List<TabelaCusto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(tabelaCustoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idTabelaCusto) {
        if (!tabelaCustoService.existsById(idTabelaCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma tabelaCusto com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tabelaCustoService.findById(idTabelaCusto));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid TabelaCustoDTO tabelaCustoDTO) {
        TabelaCusto tabelaCusto = new TabelaCusto();
        BeanUtils.copyProperties(tabelaCustoDTO, tabelaCusto);

        return ResponseEntity.status(HttpStatus.OK).body(tabelaCustoService.save(tabelaCusto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid TabelaCustoDTO tabelaCustoDTO, @PathVariable(name = "id") Integer idTabelaCusto) {
        if (!tabelaCustoService.existsById(idTabelaCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma tabelaCusto com o ID informado");
        }

        TabelaCusto tabelaCusto = tabelaCustoService.findById(idTabelaCusto).get();
        BeanUtils.copyProperties(tabelaCustoDTO, tabelaCusto);
        tabelaCusto.setIdTabelaCusto(idTabelaCusto);

        return ResponseEntity.status(HttpStatus.OK).body(tabelaCustoService.save(tabelaCusto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idTabelaCusto) {
        if (!tabelaCustoService.existsById(idTabelaCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma tabelaCusto com o ID informado");
        }
        tabelaCustoService.deleteById(idTabelaCusto);
        return ResponseEntity.status(HttpStatus.OK).body("TabelaCusto deletada com sucesso!");
    }
}
