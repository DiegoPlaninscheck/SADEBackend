//package br.weg.sod.security;
//
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@AllArgsConstructor
//public class AutenticacaoConfig {
//    private JPAService JPAService;
//
//    @Autowired
//    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(JPAService).passwordEncoder(NoOpPasswordEncoder.getInstance());
//    }
//
//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests().antMatchers("/login", "/sod/login/auth", "/logout").permitAll()
//                //ATA
//                .antMatchers(HttpMethod.GET, "/sod/ata").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.GET, "/sod/ata/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.POST, "/sod/ata").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.PUT, "/sod/ata/*/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.DELETE, "/sod/ata/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//
//                //CHAT
//                .antMatchers(HttpMethod.POST, "/sod/chat/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.PUT, "/sod/chat/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.DELETE, "/sod/chat/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//
//                //PAUTA
//                .antMatchers(HttpMethod.GET, "/sod/pauta").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.GET, "/sod/pauta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.POST, "/sod/pauta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.PUT, "/sod/pauta/*/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.DELETE, "/sod/pauta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//
//                //PROPOSTA
//                .antMatchers(HttpMethod.GET, "/sod/proposta").hasAnyAuthority("AnalistaTI", "GerenteTI", "GerenteNegocio")
//                .antMatchers(HttpMethod.GET, "/sod/proposta/*").hasAnyAuthority("AnalistaTI", "GerenteTI", "GerenteNegocio")
//                .antMatchers(HttpMethod.POST, "/sod/proposta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.PUT, "/sod/proposta/*/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//                .antMatchers(HttpMethod.DELETE, "/sod/proposta/*").hasAnyAuthority("AnalistaTI", "GerenteTI")
//
//                .anyRequest().authenticated();
//
//        httpSecurity.csrf().disable();
//
//        httpSecurity.cors().disable();
//
//        httpSecurity.formLogin()
////                .loginPage("http://127.0.0.1:8081/").defaultSuccessUrl("http://127.0.0.1:8081/home")
//                .permitAll();
//
//        httpSecurity.logout().permitAll();
//
//        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        httpSecurity.addFilterBefore(new AutenticacaoFiltro(new TokenUtils(), JPAService), UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//}
