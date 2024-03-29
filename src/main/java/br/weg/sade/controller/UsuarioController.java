package br.weg.sade.controller;

import br.weg.sade.model.dto.NotificacaoUsuarioDTO;
import br.weg.sade.model.entity.Notificacao;
import br.weg.sade.model.entity.Usuario;
import br.weg.sade.service.UsuarioService;
import br.weg.sade.util.UsuarioUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sade/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findAll());
    }

    @GetMapping("/fotousuario/{id}")
    public ResponseEntity<Object> getFotoUsuario(@PathVariable(name = "id") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findById(idUsuario).get().getFoto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findById(idUsuario));
    }

    @GetMapping("/gerente/usuario/{idUsuario}")
    public ResponseEntity<Object> findGerenteByUsuario(@PathVariable(name = "idUsuario") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findGerenteByDepartamento(usuarioService.findById(idUsuario).get().getDepartamento()));
    }

    @GetMapping("/gerente/departamento/{departamento}")
    public ResponseEntity<Object> findGerenteByDepartamento(@PathVariable(name = "departamento") String departamento) {
        Usuario usuario = usuarioService.findGerenteByDepartamento(departamento);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    @GetMapping("/gerenteTI/usuario/{idUsuario}")
    public ResponseEntity<Object> findGerenteTIByUsuario(@PathVariable(name = "idUsuario") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findGerenteTIByDepartamento(usuarioService.findById(idUsuario).get().getDepartamento()));
    }

    @GetMapping("/gerenteTI/departamento/{departamento}")
    public ResponseEntity<Object> findGerenteTIByDepartamento(@PathVariable(name = "departamento") String departamento) {
        Usuario usuario = usuarioService.findGerenteTIByDepartamento(departamento);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    @GetMapping("/{id}/chat")
    public ResponseEntity<Object> findChatsUsuario(@PathVariable(name = "id") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }

        Usuario usuario = usuarioService.findById(idUsuario).get();

        return ResponseEntity.status(HttpStatus.OK).body(usuario.getChatsUsuario());
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

    @PutMapping("/atualizarFoto/{idUsuario}")
    public ResponseEntity<Object> atualizarFoto(@RequestParam("foto") @Valid MultipartFile foto, @PathVariable(name = "idUsuario") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuario com o ID informado");
        }

        Usuario usuario = usuarioService.findById(idUsuario).get();

        try {
            usuario.setFoto(foto.getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
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
