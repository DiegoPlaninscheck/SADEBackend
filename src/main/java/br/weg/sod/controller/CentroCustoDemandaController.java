package br.weg.sod.controller;

import br.weg.sod.dto.CentroCustoDemandaDTO;
import br.weg.sod.model.entities.CentroCustoDemanda;
import br.weg.sod.model.service.CentroCustoDemandaService;
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
public class CentroCustoDemandaController {

    private CentroCustoDemandaService centroCustoDemandaService;

    @GetMapping
    public ResponseEntity<List<CentroCustoDemanda>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoDemandaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idCentroCustoDemanda) {
        if (!centroCustoDemandaService.existsById(idCentroCustoDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo demanda com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoDemandaService.findById(idCentroCustoDemanda));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CentroCustoDemandaDTO centroCustoDemandaDTO) {
        CentroCustoDemanda centroCustoDemanda = new CentroCustoDemanda();
        BeanUtils.copyProperties(centroCustoDemandaDTO, centroCustoDemanda);
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoDemandaService.save(centroCustoDemanda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid CentroCustoDemandaDTO centroCustoDemandaDTO, @PathVariable(name = "id") Integer idCentroCustoDemanda) {
        if (!centroCustoDemandaService.existsById(idCentroCustoDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo demanda com o ID informado");
        }

        CentroCustoDemanda centroCustoDemanda = centroCustoDemandaService.findById(idCentroCustoDemanda).get();
        BeanUtils.copyProperties(centroCustoDemandaDTO, centroCustoDemanda);

        return ResponseEntity.status(HttpStatus.OK).body(centroCustoDemandaService.save(centroCustoDemanda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idCentroCustoDemanda) {
        if (!centroCustoDemandaService.existsById(idCentroCustoDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum centro de custo demanda com o ID informado");
        }
        centroCustoDemandaService.deleteById(idCentroCustoDemanda);
        return ResponseEntity.status(HttpStatus.OK).body("Centro custo demanda deletado com sucesso!");
    }
}
