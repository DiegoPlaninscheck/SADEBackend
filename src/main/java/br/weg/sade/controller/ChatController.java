package br.weg.sade.controller;

import br.weg.sade.model.dto.ChatDTO;
import br.weg.sade.model.entity.Chat;
import br.weg.sade.model.entity.Demanda;
import br.weg.sade.model.entity.Notificacao;
import br.weg.sade.model.entity.Usuario;
import br.weg.sade.model.enuns.AcaoNotificacao;
import br.weg.sade.model.enuns.TipoNotificacao;
import br.weg.sade.service.ChatService;
import br.weg.sade.service.DemandaService;
import br.weg.sade.service.NotificacaoService;
import br.weg.sade.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sade/chat")
public class ChatController {

    private ChatService chatService;
    private UsuarioService usuarioService;

    private DemandaService demandaService;

    private NotificacaoService notificacaoService;

    private SimpMessagingTemplate simpMessagingTemplate;

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

    @PostMapping("/{idAnalista}")
    public ResponseEntity<Object> save(@RequestBody @Valid ChatDTO chatDTO, @PathVariable(name = "idAnalista") Integer idAnalista) {
        Chat chat = new Chat();
        BeanUtils.copyProperties(chatDTO, chat);
        
        chat.setIdChat(chatDTO.getDemanda().getIdDemanda());

        Demanda demandaChat = demandaService.findById(chat.getDemanda().getIdDemanda()).get();
        demandaChat.setTemChat(true);

        List<Usuario> usuariosRelacionados = new ArrayList<>();

        usuariosRelacionados.add(demandaChat.getUsuario());
        usuariosRelacionados.add(usuarioService.findById(idAnalista).get());
        usuariosRelacionados.add(usuarioService.findGerenteByDepartamento(demandaChat.getUsuario().getDepartamento()));

        chat.setUsuariosChat(usuariosRelacionados);

        Chat chatSalvo = chatService.save(chat);
        demandaService.save(demandaChat);

        Notificacao notificacao = new Notificacao();

        notificacao.setAcao(AcaoNotificacao.CHAT);
        notificacao.setDescricaoNotificacao("Sua demanda tem um novo chat");
        notificacao.setTituloNotificacao("Chat Criado");
        notificacao.setTipoNotificacao(TipoNotificacao.CHAT);
        notificacao.setLinkNotificacao("http://localhost:8081/home/chat");
        notificacao.setIdComponenteLink(chatSalvo.getIdChat());
        notificacao.setUsuariosNotificacao(usuariosRelacionados);

        notificacao = notificacaoService.save(notificacao);

        simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + demandaChat.getIdDemanda(), notificacao);

        return ResponseEntity.status(HttpStatus.OK).body(chatSalvo);
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
