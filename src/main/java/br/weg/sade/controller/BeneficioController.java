package br.weg.sade.controller;

import br.weg.sade.dto.BeneficioDTO;
import br.weg.sade.model.entities.Beneficio;
import br.weg.sade.model.service.BeneficioService;
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
@RequestMapping("/sade/beneficio")
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

        try {
            return checarBeneficio(beneficio);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.OK).body(exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid BeneficioDTO beneficioDTO, @PathVariable(name = "id") Integer idBeneficio) {
        if (!beneficioService.existsById(idBeneficio)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum Beneficio com o ID informado");
        }

        Beneficio beneficio = beneficioService.findById(idBeneficio).get();
        BeanUtils.copyProperties(beneficioDTO, beneficio);
        beneficio.setIdBeneficio(idBeneficio);

        try {
            return checarBeneficio(beneficio);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.OK).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idBeneficio) {
        if (!beneficioService.existsById(idBeneficio)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum Beneficio com o ID informado");
        }
        beneficioService.deleteById(idBeneficio);
        return ResponseEntity.status(HttpStatus.OK).body("Beneficio deletado com sucesso!");
    }

    /**
     * Retorna o ResponseEntity de acordo com o estado das propriedades do Benefício passado
     * Ver a função beneficioValido() em BeneficioService para mais informações
     *
     * @param beneficio
     * @return
     */
    public ResponseEntity<Object> checarBeneficio(Beneficio beneficio){
        beneficioService.beneficioValido(beneficio);

        return ResponseEntity.status(HttpStatus.OK).body(beneficioService.save(beneficio));
    }
}
