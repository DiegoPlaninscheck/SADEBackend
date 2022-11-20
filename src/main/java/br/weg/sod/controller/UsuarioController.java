package br.weg.sod.controller;

import br.weg.sod.dto.UsuarioDTO;
import br.weg.sod.model.entities.Usuario;
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
    public ResponseEntity<Object> save(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid UsuarioDTO usuarioDTO, @PathVariable(name = "id") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma usuario com o ID informado");
        }

        Usuario usuario = usuarioService.findById(idUsuario).get();
        BeanUtils.copyProperties(usuarioDTO, usuario);
        usuario.setIdUsuario(idUsuario);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
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
