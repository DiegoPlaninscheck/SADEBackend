//package br.weg.sod.security;
//
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@AllArgsConstructor
//public class AutenticacaoFiltro extends OncePerRequestFilter {
//
//    private TokenUtils tokenUtils;
//    private JPAService jpaService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println(request.getRequestURI());
//        String requestURI = request.getRequestURI();
//
//        if (requestURI.equals("/login") || requestURI.equals("/sod/login/auth") || requestURI.equals("/logout")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = tokenUtils.buscarCookie(request);
//        Boolean valido = tokenUtils.validarToken(token);
//
//        if (valido) {
//            Integer idUsuario = tokenUtils.getIDUsuario(token);
//            UserDetails usuario = jpaService.loadUserByID(idUsuario);
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usuario.getUsername(), null, usuario.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    }
//}