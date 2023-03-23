package br.weg.sod.controller;

import br.weg.sod.dto.DemandaEdicaoDTO;
import br.weg.sod.dto.NotificacaoUsuarioDTO;
import br.weg.sod.dto.UsuarioDTO;
import br.weg.sod.model.entities.Notificacao;
import br.weg.sod.model.entities.Usuario;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.service.UsuarioService;
import br.weg.sod.util.UsuarioUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/sod/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findById(idUsuario));
    }

    @PostMapping("/{tipoUsuario}")
    public ResponseEntity<Object> save(@RequestParam("usuario") String usuarioJSON, @RequestParam(value = "foto", required = false) MultipartFile foto, @PathVariable("tipoUsuario") Integer tipoUsuario) {
        UsuarioUtil usuarioUtil = new UsuarioUtil();
        Usuario usuario = usuarioUtil.convertJsonToModel(usuarioJSON, tipoUsuario);

        if (usuarioService.existsByNumeroCadastro(usuario.getNumeroCadastro())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esse número de cadastro já existe");
        }

        try {
            usuario.setFoto(foto.getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<Object> edit(@RequestParam("usuario") @Valid String usuarioJSON, @RequestParam("foto") @Valid MultipartFile foto, @PathVariable(name = "idUsuario") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }

        UsuarioUtil usuarioUtil = new UsuarioUtil();
        Usuario novoUsuario = usuarioUtil.convertJsonToModel(usuarioJSON);
        Usuario usuario = usuarioService.findById(idUsuario).get();

        BeanUtils.copyProperties(novoUsuario, usuario);
        usuario.setIdUsuario(idUsuario);

        try {
            usuario.setFoto(foto.getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
    }

    @PutMapping("/notificacao")
    public ResponseEntity<Object> novaNotificacao(@RequestBody @Valid NotificacaoUsuarioDTO notificacaoUsuarioDTO) {
        Usuario usuarioNotificacao = usuarioService.findById(notificacaoUsuarioDTO.getUsuario().getIdUsuario()).get();

        if (!usuarioService.existsById(usuarioNotificacao.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }

        List<Notificacao> novasNotificacacoesUsuario = usuarioNotificacao.getNotificacoesUsuario();
        novasNotificacacoesUsuario.add(notificacaoUsuarioDTO.getNotificacao());

        usuarioNotificacao.setNotificacoesUsuario(novasNotificacacoesUsuario);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuarioNotificacao));
    }

    @PutMapping("/deletarNotificacao")
    public ResponseEntity<Object> deletarNotificacao(@RequestBody @Valid NotificacaoUsuarioDTO notificacaoUsuarioDTO) {
        Usuario usuarioNotificacao = usuarioService.findById(notificacaoUsuarioDTO.getUsuario().getIdUsuario()).get();

        if (!usuarioService.existsById(usuarioNotificacao.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }

        List<Notificacao> novasNotificacacoesUsuario = usuarioNotificacao.getNotificacoesUsuario();

        for (int i = 0; i < novasNotificacacoesUsuario.size(); i++) {
            if (novasNotificacacoesUsuario.get(i).getIdNotificacao() == notificacaoUsuarioDTO.getNotificacao().getIdNotificacao()) {
                novasNotificacacoesUsuario.remove(i);
            }
        }

        usuarioNotificacao.setNotificacoesUsuario(novasNotificacacoesUsuario);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuarioNotificacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }
        usuarioService.deleteById(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso!");
    }
}
