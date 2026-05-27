package com.furb.sistemacursos.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furb.sistemacursos.dtos.AuthPasswordUser;
import com.furb.sistemacursos.services.AuthService;
import com.furb.sistemacursos.services.AuthToken;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final AuthToken authToken;
    private final AuthService authService;

    public AuthController(
        AuthenticationManager authenticationManager,
        AuthToken authToken,
        AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authToken = authToken;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthPasswordUser dados) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dados.getLogin(), dados.getSenha())
            );
        } catch (BadCredentialsException e) {
            log.warn("Tentativa de login falhou para '{}'", dados.getLogin());
            throw e;
        }

        UserDetails usuario = authService.loadUserByUsername(dados.getLogin());
        String token = authToken.gerarToken(usuario);
        log.info("Login bem-sucedido para '{}'", dados.getLogin());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
