package br.weg.sod.controller;

import br.weg.sod.dto.PautaDTO;
import br.weg.sod.model.entities.Pauta;
import br.weg.sod.model.service.PautaService;
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
@RequestMapping("/sod/pauta")
public class PautaController {

    private PautaService pautaService;

    @GetMapping
    public ResponseEntity<List<Pauta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findById(idPauta));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(pautaDTO, pauta);

        return ResponseEntity.status(HttpStatus.OK).body(pautaService.save(pauta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid PautaDTO pautaDTO, @PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }

        Pauta pauta = pautaService.findById(idPauta).get();
        BeanUtils.copyProperties(pautaDTO, pauta);
        pauta.setIdPauta(idPauta);

        return ResponseEntity.status(HttpStatus.OK).body(pautaService.save(pauta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }
        pautaService.deleteById(idPauta);
        return ResponseEntity.status(HttpStatus.OK).body("Pauta deletada com sucesso!");
    }
}
