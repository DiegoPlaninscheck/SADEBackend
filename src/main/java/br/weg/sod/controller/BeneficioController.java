package br.weg.sod.controller;

import br.weg.sod.dto.BeneficioDTO;
import br.weg.sod.model.entities.Beneficio;
import br.weg.sod.model.service.BeneficioService;
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
@RequestMapping("/sod/beneficio")
public class BeneficioController {

    private BeneficioService beneficioService;

    @GetMapping
    public ResponseEntity<List<Beneficio>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(beneficioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idBeneficio) {
        if (!beneficioService.existsById(idBeneficio)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum Beneficio com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(beneficioService.findById(idBeneficio));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BeneficioDTO beneficioDTO) {
        Beneficio beneficio = new Beneficio();
        BeanUtils.copyProperties(beneficioDTO, beneficio);
        return ResponseEntity.status(HttpStatus.OK).body(beneficioService.save(beneficio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid BeneficioDTO beneficioDTO, @PathVariable(name = "id") Integer idBeneficio) {
        if (!beneficioService.existsById(idBeneficio)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum Beneficio com o ID informado");
        }

        Beneficio beneficio = beneficioService.findById(idBeneficio).get();
        BeanUtils.copyProperties(beneficioDTO, beneficio);
        beneficio.setIdBeneficio(idBeneficio);

        return ResponseEntity.status(HttpStatus.OK).body(beneficioService.save(beneficio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idBeneficio) {
        if (!beneficioService.existsById(idBeneficio)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum Beneficio com o ID informado");
        }
        beneficioService.deleteById(idBeneficio);
        return ResponseEntity.status(HttpStatus.OK).body("Beneficio deletado com sucesso!");
    }
}