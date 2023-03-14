//package br.weg.sod.security;
//
//import br.weg.sod.model.entities.Usuario;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//
//@Controller
//@RequestMapping("/sod/login")
//public class AutenticacaoController {
//
//    private TokenUtils tokenUtils = new TokenUtils();
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @PostMapping("/auth")
//    public ResponseEntity<Object> autenticacao(@RequestBody @Valid UsuarioDTO usuarioDTO, HttpServletResponse response) {
//        System.out.println(usuarioDTO);
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha());
//        Authentication authentication = authenticationManager.authenticate(authenticationToken);
//
//        System.out.println(authentication.isAuthenticated());
//
//        if (authentication.isAuthenticated()) {
//            String token = tokenUtils.gerarToken(authentication);
//            Cookie cookie = new Cookie("jwt", token);
//            UserJPA userJPA = (UserJPA) authentication.getPrincipal();
//            Usuario usuario = userJPA.getUsuario();
//            response.addCookie(cookie);
//            return ResponseEntity.ok(usuario);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//
//}