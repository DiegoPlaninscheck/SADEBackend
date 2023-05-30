package br.weg.sade.controller;

import br.weg.sade.dto.LinhaTabelaDTO;
import br.weg.sade.model.entities.LinhaTabela;
import br.weg.sade.model.service.LinhaTabelaSevice;
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
@RequestMapping("/sade/linhaTabela")
public class LinhaTabelaController {

    private LinhaTabelaSevice linhaTabelaSevice;

    @GetMapping
    public ResponseEntity<List<LinhaTabela>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(linhaTabelaSevice.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idLinhaTabela) {
        if (!linhaTabelaSevice.existsById(idLinhaTabela)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma linha tabela com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(linhaTabelaSevice.findById(idLinhaTabela));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid LinhaTabelaDTO linhaTabelaDTO) {
        LinhaTabela linhaTabela = new LinhaTabela();
        BeanUtils.copyProperties(linhaTabelaDTO, linhaTabela);
        return ResponseEntity.status(HttpStatus.OK).body(linhaTabelaSevice.save(linhaTabela));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid LinhaTabelaDTO linhaTabelaDTO, @PathVariable(name = "id") Integer idLinhaTabela) {
        if (!linhaTabelaSevice.existsById(idLinhaTabela)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma linha tabela com o ID informado");
        }

        LinhaTabela linhaTabela = linhaTabelaSevice.findById(idLinhaTabela).get();
        BeanUtils.copyProperties(linhaTabelaDTO, linhaTabela);
        linhaTabela.setIdLinhaTabela(idLinhaTabela);

        return ResponseEntity.status(HttpStatus.OK).body(linhaTabelaSevice.save(linhaTabela));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idLinhaTabela) {
        if (!linhaTabelaSevice.existsById(idLinhaTabela)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma linha tabela com o ID informado");
        }
        linhaTabelaSevice.deleteById(idLinhaTabela);
        return ResponseEntity.status(HttpStatus.OK).body("Linha Tabela deletado com sucesso!");
    }
}
