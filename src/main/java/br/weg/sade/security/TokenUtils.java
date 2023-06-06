package br.weg.sade.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;



public class TokenUtils {
    private final String senhaForte = "f9fae8edc4c43f2bdf83b5f3c37a80d8f3c73f559785889e606387be23558867";

    public String gerarToken(String subject, Integer expirationDate) {
        return Jwts.builder().setIssuer("Sod")
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationDate))
                .signWith(SignatureAlgorithm.HS256, senhaForte).compact();
    }

    public Cookie gerarCookie(String subject, String nomeCookie, Integer maxAge) {
        Cookie cookie = new Cookie(nomeCookie, gerarToken(subject, maxAge * 1000));

        cookie.setVersion(cookie.getVersion() + 1);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    public Boolean validarToken(String token) {
        try {
            decodarToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Object decodarToken(String token){
        return Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
    }

    public Integer getIDUsuario(String token) {
        return Integer.parseInt(Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token).getBody().getSubject());
    }

    public String buscarCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "jwt");

        if (cookie != null) {
            return cookie.getValue();
        }

        cookie = WebUtils.getCookie(request, "rjwt");
        if (cookie != null) {
            return cookie.getValue();
        }

        throw new RuntimeException("Cookie não encontrado!");
    }

    public String buscarCookie(HttpServletRequest request, String nomeCookie) {
        Cookie cookie = WebUtils.getCookie(request, nomeCookie);

        if (cookie != null) {
            return cookie.getValue();
        }

        throw new RuntimeException("Cookie não encontrado!");
    }
}
