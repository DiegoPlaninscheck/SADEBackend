package br.weg.sod.controller;

import br.weg.sod.dto.DemandaDTO;
import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.service.DemandaService;
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
@RequestMapping("/sod/demanda")
public class DemandaController {

    private DemandaService demandaService;

    @GetMapping
    public ResponseEntity<List<Demanda>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findById(idDemanda));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DemandaDTO demandaDTO) {
        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(demandaDTO, demanda);

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid DemandaDTO demandaDTO, @PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        Demanda demanda = demandaService.findById(idDemanda).get();
        BeanUtils.copyProperties(demandaDTO, demanda);
        demanda.setIdDemanda(idDemanda);

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }
        demandaService.deleteById(idDemanda);
        return ResponseEntity.status(HttpStatus.OK).body("Demanda deletada com sucesso!");
    }
}
