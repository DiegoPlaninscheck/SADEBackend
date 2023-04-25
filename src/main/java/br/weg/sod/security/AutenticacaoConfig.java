package br.weg.sod.security;

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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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
        authenticationManagerBuilder.userDetailsService(JPAService).passwordEncoder(NoOpPasswordEncoder.getInstance());
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
        httpSecurity.authorizeRequests().antMatchers("/login", "/sod/login/auth", "/logout").permitAll()
                //ATA
                .antMatchers(HttpMethod.GET, "/sod/ata").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.GET, "/sod/ata/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.POST, "/sod/ata").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.PUT, "/sod/ata/**").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.DELETE, "/sod/ata/*").hasAnyAuthority("AnalistaTI", "GerenteTI")

                //CHAT
                .antMatchers(HttpMethod.POST, "/sod/chat/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.PUT, "/sod/chat/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.DELETE, "/sod/chat/*").hasAnyAuthority("AnalistaTI", "GerenteTI")

                //PAUTA
                .antMatchers(HttpMethod.GET, "/sod/pauta").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.GET, "/sod/pauta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.POST, "/sod/pauta/**").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.PUT, "/sod/pauta/**").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.DELETE, "/sod/pauta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")

                //PROPOSTA
                .antMatchers(HttpMethod.GET, "/sod/proposta").hasAnyAuthority("AnalistaTI", "GerenteTI", "GerenteNegocio")
                .antMatchers(HttpMethod.GET, "/sod/proposta/*").hasAnyAuthority("AnalistaTI", "GerenteTI", "GerenteNegocio")
                .antMatchers(HttpMethod.POST, "/sod/proposta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.PUT, "/sod/proposta/**").hasAnyAuthority("AnalistaTI", "GerenteTI")
                .antMatchers(HttpMethod.DELETE, "/sod/proposta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")

                //SWAGGER
                .antMatchers("/swagger-ui**", "/swagger-ui/**", "/v3/api-docs/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated();

        httpSecurity.csrf().disable();

        httpSecurity.cors().configurationSource(corsConfigurationSource());;

//        httpSecurity.formLogin()
//                .loginPage("http://127.0.0.1:8081/").defaultSuccessUrl("http://127.0.0.1:8081/home")
//                .permitAll();

        httpSecurity.logout().permitAll();

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(new AutenticacaoFiltro(new TokenUtils(), JPAService), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
