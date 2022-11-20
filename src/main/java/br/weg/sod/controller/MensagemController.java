package br.weg.sod.controller;

import br.weg.sod.dto.MensagemDTO;
import br.weg.sod.model.entities.Mensagem;
import br.weg.sod.model.service.MensagemService;
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
@RequestMapping("/sod/mensagem")
public class MensagemController {

    private MensagemService mensagemService;

    @GetMapping
    public ResponseEntity<List<Mensagem>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(mensagemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idMensagem) {
        if (!mensagemService.existsById(idMensagem)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma mensagem com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(mensagemService.findById(idMensagem));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid MensagemDTO mensagemDTO) {
        Mensagem mensagem = new Mensagem();
        BeanUtils.copyProperties(mensagemDTO, mensagem);

        return ResponseEntity.status(HttpStatus.OK).body(mensagemService.save(mensagem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid MensagemDTO mensagemDTO, @PathVariable(name = "id") Integer idMensagem) {
        if (!mensagemService.existsById(idMensagem)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma mensagem com o ID informado");
        }

        Mensagem mensagem = mensagemService.findById(idMensagem).get();
        BeanUtils.copyProperties(mensagemDTO, mensagem);
        mensagem.setIdMensagem(idMensagem);

        return ResponseEntity.status(HttpStatus.OK).body(mensagemService.save(mensagem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idMensagem) {
        if (!mensagemService.existsById(idMensagem)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma mensagem com o ID informado");
        }
        mensagemService.deleteById(idMensagem);
        return ResponseEntity.status(HttpStatus.OK).body("Mensagem deletada com sucesso!");
    }

}
