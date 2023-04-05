package br.weg.sod.security;

import br.weg.sod.model.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/sod/login")
public class AutenticacaoController {

    private TokenUtils tokenUtils = new TokenUtils();

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<Object> autenticacao(@RequestBody @Valid UsuarioDTO usuarioDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (authentication.isAuthenticated()) {
            response.addCookie(tokenUtils.gerarCookie(authentication));
            UserJPA userJPA = (UserJPA) authentication.getPrincipal();
            return ResponseEntity.ok(userJPA.getUsuario());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}