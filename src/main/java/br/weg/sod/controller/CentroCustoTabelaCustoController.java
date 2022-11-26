package br.weg.sod.controller;

import br.weg.sod.dto.CentroCustoTabelaCustoDTO;
import br.weg.sod.model.entities.CentroCustoTabelaCusto;
import br.weg.sod.model.service.CentroCustoTabelaCustoService;
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
@RequestMapping("/sod/centroCustoDemanda")
public class CentroCustoTabelaCustoController {

    private CentroCustoTabelaCustoService centroCustoTabelaCustoService;

    @GetMapping
    public ResponseEntity<List<CentroCustoTabelaCusto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoTabelaCustoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idCentroCustoTabelaCusto) {
        if (!centroCustoTabelaCustoService.existsById(idCentroCustoTabelaCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo tabela custo com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoTabelaCustoService.findById(idCentroCustoTabelaCusto));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CentroCustoTabelaCustoDTO centroCustoTabelaCustoDTO) {
        CentroCustoTabelaCusto centroCustoTabelaCusto = new CentroCustoTabelaCusto();
        BeanUtils.copyProperties(centroCustoTabelaCustoDTO, centroCustoTabelaCusto);
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoTabelaCustoService.save(centroCustoTabelaCusto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid CentroCustoTabelaCustoDTO centroCustoTabelaCustoDTO, @PathVariable(name = "id") Integer idCentroCustoTabelaCusto) {
        if (!centroCustoTabelaCustoService.existsById(idCentroCustoTabelaCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo tabela custo com o ID informado");
        }

        CentroCustoTabelaCusto centroCustoTabelaCusto = centroCustoTabelaCustoService.findById(idCentroCustoTabelaCusto).get();
        BeanUtils.copyProperties(centroCustoTabelaCustoDTO, centroCustoTabelaCusto);

        return ResponseEntity.status(HttpStatus.OK).body(centroCustoTabelaCustoService.save(centroCustoTabelaCusto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idCentroCustoTabelaCusto) {
        if (!centroCustoTabelaCustoService.existsById(idCentroCustoTabelaCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo tabela custo com o ID informado");
        }
        centroCustoTabelaCustoService.deleteById(idCentroCustoTabelaCusto);
        return ResponseEntity.status(HttpStatus.OK).body("Centro custo demanda deletado com sucesso!");
    }
}
