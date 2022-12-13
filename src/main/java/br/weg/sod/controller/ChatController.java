package br.weg.sod.controller;

import br.weg.sod.dto.ChatDTO;
import br.weg.sod.model.entities.AnalistaTI;
import br.weg.sod.model.entities.Chat;
import br.weg.sod.model.service.ChatService;
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
@RequestMapping("/sod/chat")
public class ChatController {

    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<Chat>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idChat) {
        if (!chatService.existsById(idChat)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum chat com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(chatService.findById(idChat));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ChatDTO chatDTO) {
        Chat chat = new Chat();
        BeanUtils.copyProperties(chatDTO, chat);
        chat.setIdChat(chatDTO.getDemanda().getIdDemanda());

        return ResponseEntity.status(HttpStatus.OK).body(chatService.save(chat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody @Valid ChatDTO chatDTO, @PathVariable(name = "id") Integer idChat) {
        if (!chatService.existsById(idChat)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum chat com o ID informado");
        }

        Chat chat = chatService.findById(idChat).get();
        BeanUtils.copyProperties(chatDTO, chat);
        chat.setIdChat(idChat);

        return ResponseEntity.status(HttpStatus.OK).body(chatService.save(chat));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idChat) {
        if (!chatService.existsById(idChat)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum chat com o ID informado");
        }
        chatService.deleteById(idChat);
        return ResponseEntity.status(HttpStatus.OK).body("Chat deletado com sucesso!");
    }
}
