package br.weg.sade.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {

    private TokenUtils tokenUtils;
    private JPAService jpaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/login") || requestURI.equals("/sade/login/auth") || requestURI.equals("/sade/login/auth/cookie") || requestURI.equals("/logout")
                || requestURI.startsWith("/swagger-ui")  || requestURI.startsWith("/v3/api-docs") ||  requestURI.startsWith("/favicon.ico")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = tokenUtils.buscarCookie(request);
        Boolean valido = tokenUtils.validarToken(token);

        if (valido) {
            Integer idUsuario = tokenUtils.getIDUsuario(token);
            UserDetails usuario = jpaService.loadUserByID(idUsuario);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword(), usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            Cookie jwtCookie = tokenUtils.gerarCookie((UserJPA) usuario, "jwt", 14400);
            response.addCookie(jwtCookie);

            if(request.getCookies() != null){
                for(Cookie cookie : request.getCookies()){
                    if(cookie.getName() == "rjwt"){
                        Cookie rjwtCookie = tokenUtils.gerarCookie((UserJPA) usuario, "rjwt", 604800);
                        response.addCookie(rjwtCookie);
                    }
                }
            }

            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
