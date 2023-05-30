package br.weg.sade.controller;

import br.weg.sade.dto.CentroCustoPaganteDTO;
import br.weg.sade.model.entities.CentroCustoPagante;
import br.weg.sade.model.service.CentroCustoPaganteService;
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
@RequestMapping("/sade/centroCustoPagante")
public class CentroCustoPaganteController {

    private CentroCustoPaganteService centroCustoPaganteService;

    @GetMapping
    public ResponseEntity<List<CentroCustoPagante>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoPaganteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idCentroCustoTabelaCusto) {
        if (!centroCustoPaganteService.existsById(idCentroCustoTabelaCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo tabela custo com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoPaganteService.findById(idCentroCustoTabelaCusto));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CentroCustoPaganteDTO centroCustoPaganteDTO) {
        CentroCustoPagante centroCustoPagante = new CentroCustoPagante();
        BeanUtils.copyProperties(centroCustoPaganteDTO, centroCustoPagante);
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoPaganteService.save(centroCustoPagante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid CentroCustoPaganteDTO centroCustoPaganteDTO, @PathVariable(name = "id") Integer idCentroCustoTabelaCusto) {
        if (!centroCustoPaganteService.existsById(idCentroCustoTabelaCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo tabela custo com o ID informado");
        }

        CentroCustoPagante centroCustoPagante = centroCustoPaganteService.findById(idCentroCustoTabelaCusto).get();
        BeanUtils.copyProperties(centroCustoPaganteDTO, centroCustoPagante);

        return ResponseEntity.status(HttpStatus.OK).body(centroCustoPaganteService.save(centroCustoPagante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idCentroCustoTabelaCusto) {
        if (!centroCustoPaganteService.existsById(idCentroCustoTabelaCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo tabela custo com o ID informado");
        }
        centroCustoPaganteService.deleteById(idCentroCustoTabelaCusto);
        return ResponseEntity.status(HttpStatus.OK).body("Centro custo demanda deletado com sucesso!");
    }
}
