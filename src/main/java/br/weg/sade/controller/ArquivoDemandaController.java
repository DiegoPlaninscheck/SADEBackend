package br.weg.sade.controller;

import br.weg.sade.dto.ArquivoDemandaDTO;
import br.weg.sade.model.entities.ArquivoDemanda;
import br.weg.sade.model.service.ArquivoDemandaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sade/arquivoDemanda")
public class ArquivoDemandaController {

    @Autowired
    private ArquivoDemandaService arquivoDemandaService;

    @GetMapping
    public ResponseEntity<List<ArquivoDemanda>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(arquivoDemandaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idArquivoDemanda) {
        if (!arquivoDemandaService.existsById(idArquivoDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum arquivoDemanda com este ID");
        }

        return ResponseEntity.status(HttpStatus.OK).body(arquivoDemandaService.findById(idArquivoDemanda));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ArquivoDemandaDTO arquivoDemandaDTO) {
        ArquivoDemanda arquivoDemanda = new ArquivoDemanda();
        BeanUtils.copyProperties(arquivoDemandaDTO, arquivoDemanda);
        return ResponseEntity.status(HttpStatus.OK).body(arquivoDemanda);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid ArquivoDemandaDTO arquivoDemandaDTO, @PathVariable(name = "id") Integer idArquivoDemanda) {
        if (!arquivoDemandaService.existsById(idArquivoDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum arquivoDemanda com o ID informado");
        }

        ArquivoDemanda arquivoDemanda = arquivoDemandaService.findById(idArquivoDemanda).get();
        BeanUtils.copyProperties(arquivoDemandaDTO, arquivoDemanda);
        arquivoDemanda.setIdArquivoDemanda(idArquivoDemanda);

        return ResponseEntity.status(HttpStatus.OK).body(arquivoDemandaService.save(arquivoDemanda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idArquivoDemanda) {
        if (!arquivoDemandaService.existsById(idArquivoDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum arquivoDemanda com este ID");
        }
        arquivoDemandaService.deleteById(idArquivoDemanda);
        return ResponseEntity.status(HttpStatus.OK).body("Arquivo demanda deletada com sucesso!");
    }
}
