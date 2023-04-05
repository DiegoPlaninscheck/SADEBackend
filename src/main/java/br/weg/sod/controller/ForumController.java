package br.weg.sod.controller;

import br.weg.sod.dto.ForumDTO;
import br.weg.sod.model.entities.Forum;
import br.weg.sod.model.service.ForumService;
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
@RequestMapping("/sod/forum")
public class ForumController {

    private ForumService forumService;

    @GetMapping
    public ResponseEntity<List<Forum>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(forumService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idForum) {
        if (!forumService.existsById(idForum)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum forum com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(forumService.findById(idForum));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ForumDTO forumDTO) {
        Forum forum = new Forum();
        BeanUtils.copyProperties(forumDTO, forum);

        return ResponseEntity.status(HttpStatus.OK).body(forumService.save(forum));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid ForumDTO forumDTO, @PathVariable(name = "id") Integer idForum) {
        if (!forumService.existsById(idForum)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum forum com o ID informado");
        }

        Forum forum = forumService.findById(idForum).get();
        BeanUtils.copyProperties(forumDTO, forum);
        forum.setIdForum(idForum);

        return ResponseEntity.status(HttpStatus.OK).body(forumService.save(forum));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idForum) {
        if (!forumService.existsById(idForum)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum forum com o ID informado");
        }
        forumService.deleteById(idForum);
        return ResponseEntity.status(HttpStatus.OK).body("Forum deletado com sucesso!");
    }
}
