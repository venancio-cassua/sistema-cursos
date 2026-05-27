package com.furb.sistemacursos.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.furb.sistemacursos.services.AuthService;
import com.furb.sistemacursos.services.AuthToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class httpRequestFilter
extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(httpRequestFilter.class);

    private final AuthToken authToken;
    private final AuthService authService;

    public httpRequestFilter(AuthToken authToken, AuthService authService){
        this.authToken = authToken;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("{} {} - sem token", method, uri);
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            UserDetails user = authService.loadUserByUsername(authToken.extrairLogin(token));

            if (!authToken.tokenValido(token, user)) {
                throw new RuntimeException("Token inválido");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("{} {} - autenticado como '{}'", method, uri, user.getUsername());
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.warn("{} {} - token inválido: {}", method, uri, e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido");
        }
    }
}