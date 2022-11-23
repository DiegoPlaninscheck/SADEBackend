package br.weg.sod.controller;

import br.weg.sod.dto.UsuarioDTO;
import br.weg.sod.model.entities.Usuario;
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma usuario com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findById(idUsuario));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam("usuario") String usuarioJSON, @RequestParam("foto") MultipartFile foto) {
        UsuarioUtil usuarioUtil = new UsuarioUtil();
        Usuario usuario = usuarioUtil.convertJsonToModel(usuarioJSON);

        System.out.println(usuario);

        try {
            usuario.setFoto(foto.getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestParam("usuario") @Valid String usuarioJSON, @RequestParam("foto") @Valid MultipartFile foto, @PathVariable(name = "id") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma usuario com o ID informado");
        }

        UsuarioUtil usuarioUtil = new UsuarioUtil();
        Usuario novoUsuario = usuarioUtil.convertJsonToModel(usuarioJSON);
        Usuario usuario = usuarioService.findById(idUsuario).get();
        System.out.println("novo: " + novoUsuario);
        System.out.println("velho: " + usuario);
        BeanUtils.copyProperties(novoUsuario, usuario);
        usuario.setIdUsuario(idUsuario);
        try {
            usuario.setFoto(foto.getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario).toString());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma usuario com o ID informado");
        }
        usuarioService.deleteById(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso!");
    }
}
