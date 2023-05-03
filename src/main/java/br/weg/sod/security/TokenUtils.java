package br.weg.sod.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;



public class TokenUtils {
    private final String senhaForte = "f9fae8edc4c43f2bdf83b5f3c37a80d8f3c73f559785889e606387be23558867";

    public String gerarToken(Authentication authentication) {
        UserJPA userJPA = (UserJPA) authentication.getPrincipal();

        return Jwts.builder().setIssuer("Sod")
                .setSubject(userJPA.getUsuario().getIdUsuario().toString())
                .setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + 1800000))
                .signWith(SignatureAlgorithm.HS256, senhaForte).compact()
                ;
    }

    public Cookie gerarCookie(Authentication authentication) {
        Cookie cookie = new Cookie("jwt", gerarToken(authentication));
        cookie.setPath("/");
        cookie.setMaxAge(7200);
        return cookie;
    }

    public Cookie renovarCookie(HttpServletRequest request, String nome){
        Cookie cookie = WebUtils.getCookie(request, nome);
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        return cookie;
    }

    public Boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer getIDUsuario(String token) {
        return Integer.parseInt(Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token).getBody().getSubject());
    }

    public String buscarCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "jwt");

        if (cookie != null) {
            return cookie.getValue();
        }
        throw new RuntimeException("Cookie não encontrado!");
    }
}
