package br.weg.sod.controller;

import br.weg.sod.dto.NotificacaoDTO;
import br.weg.sod.dto.NotificacaoUsuarioDTO;
import br.weg.sod.model.entities.Notificacao;
import br.weg.sod.model.entities.Usuario;
import br.weg.sod.model.entities.enuns.TipoNotificacao;
import br.weg.sod.model.service.NotificacaoService;
import br.weg.sod.model.service.UsuarioService;
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
@RestController
@RequestMapping("/sod/notificacao")
public class NotificacaoController {

    private NotificacaoService notificacaoService;
    private UsuarioController usuarioController;
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Notificacao>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idNotificacao) {
        if (!notificacaoService.existsById(idNotificacao)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma notificacao com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.findById(idNotificacao));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid NotificacaoDTO notificacaoDTO) {
        Notificacao notificacao = new Notificacao();
        BeanUtils.copyProperties(notificacaoDTO, notificacao);
        Notificacao notificacaoSalva = notificacaoService.save(notificacao);

        for (Usuario usuario : notificacaoDTO.getUsuariosRelacionados()) {
            NotificacaoUsuarioDTO notificacaoUsuarioDTO = new NotificacaoUsuarioDTO(notificacaoSalva, usuario);
            usuarioController.novaNotificacao(notificacaoUsuarioDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(notificacaoSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid NotificacaoDTO notificacaoDTO, @PathVariable(name = "id") Integer idNotificacao) {
        if (!notificacaoService.existsById(idNotificacao)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma notificacao com o ID informado");
        }

        Notificacao notificacao = notificacaoService.findById(idNotificacao).get();
        BeanUtils.copyProperties(notificacaoDTO, notificacao);
        notificacao.setIdNotificacao(idNotificacao);

        return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.save(notificacao));
    }

    @DeleteMapping("/{idNotificacao}/{idUsuario}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "idNotificacao") Integer idNotificacao, @PathVariable(name = "idUsuario") Integer idUsuario) {
        if (!notificacaoService.existsById(idNotificacao)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma notificacao com o ID informado");
        }

        Usuario usuario = usuarioService.findById(idUsuario).get();

        notificacaoService.deleteById(idNotificacao);

        return ResponseEntity.status(HttpStatus.OK).body(usuario.getNotificacoesUsuario());
    }

}
