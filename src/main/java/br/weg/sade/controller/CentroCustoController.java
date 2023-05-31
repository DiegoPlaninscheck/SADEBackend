package br.weg.sade.controller;

import br.weg.sade.model.dto.CentroCustoDTO;
import br.weg.sade.model.entity.CentroCusto;
import br.weg.sade.service.CentroCustoService;
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
@RequestMapping("/sade/centroCusto")
public class CentroCustoController {

    private CentroCustoService centroCustoService;

    @GetMapping
    public ResponseEntity<List<CentroCusto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idCentroCusto) {
        if (!centroCustoService.existsById(idCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoService.findById(idCentroCusto));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CentroCustoDTO centroCustoDTO) {
        CentroCusto centroCusto = new CentroCusto();
        BeanUtils.copyProperties(centroCustoDTO, centroCusto);
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoService.save(centroCusto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid CentroCustoDTO centroCustoDTO, @PathVariable(name = "id") Integer idCentroCusto) {
        if (!centroCustoService.existsById(idCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo com o ID informado");
        }

        CentroCusto centroCusto = centroCustoService.findById(idCentroCusto).get();
        BeanUtils.copyProperties(centroCustoDTO, centroCusto);
        centroCusto.setIdCentroCusto(idCentroCusto);

        return ResponseEntity.status(HttpStatus.OK).body(centroCustoService.save(centroCusto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idCentroCusto) {
        if (!centroCustoService.existsById(idCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo com o ID informado");
        }
        centroCustoService.deleteById(idCentroCusto);
        return ResponseEntity.status(HttpStatus.OK).body("Centro de custo deletado com sucesso!");
    }
}
