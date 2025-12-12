package com.herramientas.recetas_saludables.security;

import com.herramientas.recetas_saludables.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/",
                                "/login",
                                "/registro",
                                "/usuario/perfil",
                                "/buscar",
                                "/recetas",
                                "/vista/**",
                                "/usuario/guardarUsuario",
                                "/api/ingredientes/**",
                                "/api/recetas/buscar",
                                "/api/recetas/**",
                                "/assets/**",
                                "/css/**",
                                "/js/**")
                        .permitAll()
                        .requestMatchers("/api/usuarios/favoritos/**")
                        .authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login") // ruta del POST del formulario
                        .usernameParameter("correo") // ⚠️ decimos a Spring que use "email"
                        .passwordParameter("contrasena") // nombre del input
                        .defaultSuccessUrl("/usuario/perfil", true)
                        .permitAll())
                .logout(logout -> logout.permitAll())
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

}