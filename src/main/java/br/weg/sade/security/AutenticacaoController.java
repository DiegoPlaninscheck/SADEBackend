package br.weg.sade.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/sade/login")
public class AutenticacaoController {

    private TokenUtils tokenUtils = new TokenUtils();

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/cookie/{token}")
    public ResponseEntity<Object> transformarToken(@PathVariable("token") String token){




        return ResponseEntity.ok().body(tokenUtils.decodarToken(token));
    }

    @PostMapping("/auth")
    public ResponseEntity<Object> autenticacao(@RequestBody @Valid UsuarioDTO usuarioDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (authentication.isAuthenticated()) {
            UserJPA userJPA = (UserJPA) authentication.getPrincipal();
            response.addCookie(tokenUtils.gerarCookie(userJPA.getUsuario().getIdUsuario().toString(), "jwt", 14400));
            return ResponseEntity.ok(userJPA);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/auth/cookie")
    public ResponseEntity<Object> autenticacaoCookie(@RequestBody @Valid UsuarioDTO usuarioDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (authentication.isAuthenticated()) {
            Cookie cookie = tokenUtils.gerarCookie(usuarioDTO.getStringUsuario(), "rjwt", 604800);
            response.addCookie(cookie);
            return ResponseEntity.ok(cookie);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}