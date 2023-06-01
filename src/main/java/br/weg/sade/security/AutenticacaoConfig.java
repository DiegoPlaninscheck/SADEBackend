package br.weg.sade.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class AutenticacaoConfig {
    private JPAService JPAService;

    @Autowired
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(JPAService).passwordEncoder(new BCryptPasswordEncoder());
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://127.0.0.1:8081", "http://localhost:8081"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/login", "/sade/login/auth", "/sade/login/auth/cookie", "/logout").permitAll()
                //ATA
                .antMatchers(HttpMethod.GET, "/sade/ata").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.GET, "/sade/ata/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.POST, "/sade/ata").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.PUT, "/sade/ata/**").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.DELETE, "/sade/ata/*").hasAnyAuthority("AnalistaTI", "GerenteTI")

                //CHAT
                .antMatchers(HttpMethod.POST, "/sade/chat/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.PUT, "/sade/chat/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.DELETE, "/sade/chat/*").hasAnyAuthority("AnalistaTI", "GerenteTI")

                //PAUTA
                .antMatchers(HttpMethod.GET, "/sade/pauta").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.GET, "/sade/pauta/**").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.POST, "/sade/pauta/**").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.PUT, "/sade/pauta/**").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.DELETE, "/sade/pauta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")

                //PROPOSTA
                .antMatchers(HttpMethod.GET, "/sade/proposta").hasAnyAuthority("AnalistaTI", "GerenteTI", "GerenteNegocio")
                .antMatchers(HttpMethod.GET, "/sade/proposta/*").hasAnyAuthority("AnalistaTI", "GerenteTI", "GerenteNegocio")
                .antMatchers(HttpMethod.POST, "/sade/proposta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.PUT, "/sade/proposta/**").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.DELETE, "/sade/proposta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")

                //SWAGGER
                .antMatchers("/swagger-ui**", "/swagger-ui/**", "/v3/api-docs/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated();

        httpSecurity.csrf().disable();

        httpSecurity.cors().configurationSource(corsConfigurationSource());;

//        httpSecurity.formLogin()
//                .loginPage("http://127.0.0.1:8081/").defaultSuccessUrl("http://127.0.0.1:8081/home")
//                .permitAll();

        httpSecurity.logout().logoutUrl("/logout").deleteCookies("jwt", "rjwt").permitAll();

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(new AutenticacaoFiltro(new TokenUtils(), JPAService), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
