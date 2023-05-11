package br.weg.sod.security;

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

        System.out.println(requestURI);

        if (requestURI.equals("/login") || requestURI.equals("/sod/login/auth") || requestURI.equals("/sod/login/auth/cookie") || requestURI.equals("/logout")
                || requestURI.startsWith("/swagger-ui")  || requestURI.startsWith("/v3/api-docs") ||  requestURI.startsWith("/favicon.ico")) {
            System.out.println("rota livre");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("rota autenticada");

        String token = tokenUtils.buscarCookie(request);
        Boolean valido = tokenUtils.validarToken(token);

        if (valido) {
            System.out.println("validou");
            Integer idUsuario = tokenUtils.getIDUsuario(token);
            UserDetails usuario = jpaService.loadUserByID(idUsuario);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usuario.getUsername(), null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            Cookie jwtCookie = tokenUtils.renovarCookie(request, "jwt");

            System.out.println(jwtCookie.getName() + " / " + jwtCookie.getMaxAge());
            response.addCookie(jwtCookie);

            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
