package br.weg.sade.controller;

import br.weg.sade.dto.ChatDTO;
import br.weg.sade.dto.NotificacaoDTO;
import br.weg.sade.dto.NotificacaoUsuarioDTO;
import br.weg.sade.model.entities.Chat;
import br.weg.sade.model.entities.Notificacao;
import br.weg.sade.model.entities.Usuario;
import br.weg.sade.model.entities.enuns.AcaoNotificacao;
import br.weg.sade.model.entities.enuns.TipoNotificacao;
import br.weg.sade.model.service.NotificacaoService;
import br.weg.sade.model.service.UsuarioService;
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
@RequestMapping("/sade/notificacao")
public class NotificacaoController {

    private NotificacaoService notificacaoService;
    private UsuarioController usuarioController;
    private UsuarioService usuarioService;

    private SimpMessagingTemplate simpMessagingTemplate;

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

    @PostMapping("/chat/{id}")
    public ResponseEntity<Object> salvarNotificacaoChat(@RequestBody @Valid ChatDTO chatDTO, @PathVariable(name = "id") Integer idChat){
        Chat chat = new Chat();

        BeanUtils.copyProperties(chatDTO, chat);

        Notificacao notificacao = new Notificacao();
        notificacao.setAcao(AcaoNotificacao.CHAT);
        notificacao.setDescricaoNotificacao("Há nova(s) mensagem(ns) no seu chat!");
        notificacao.setTituloNotificacao("Chat");
        notificacao.setTipoNotificacao(TipoNotificacao.CHAT);
        notificacao.setLinkNotificacao("http://localhost:8081/chats");

        List<Usuario> usuarios = new ArrayList<>();

        for (Usuario usuario : chat.getUsuariosChat()){
            usuarios.add(usuario);
        }

        notificacao.setUsuariosNotificacao(usuarios);

        notificacao.setIdComponenteLink(idChat);

        Notificacao notificacaoSalva = notificacaoService.save(notificacao);

        simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + chat.getDemanda().getIdDemanda(), notificacao);

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
