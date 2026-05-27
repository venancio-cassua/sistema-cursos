package com.furb.sistemacursos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final httpRequestFilter
        filterAuth;

    public SecurityConfig(
        httpRequestFilter filterAuth
    ) {
        this.filterAuth =
            filterAuth;
    }

    @Bean
    public SecurityFilterChain
    securityFilterChain(
        HttpSecurity http
    ) throws Exception {

        return http
            .csrf(csrf ->
                csrf.disable()
            )

            .sessionManagement(
                session ->
                    session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                    )
            )

            .authorizeHttpRequests(auth ->
                auth
                    .requestMatchers("/auth/**")
                    .permitAll()

                    .requestMatchers(org.springframework.http.HttpMethod.POST, "/usuario")
                    .permitAll()

                    .anyRequest()
                    .authenticated()
            )

            .addFilterBefore(
                filterAuth,
                UsernamePasswordAuthenticationFilter.class
            )
            .exceptionHandling(ex ->
                ex.authenticationEntryPoint(
                    (request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Não autenticado")
                )
            )


            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}