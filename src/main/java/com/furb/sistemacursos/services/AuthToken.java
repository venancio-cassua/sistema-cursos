package com.furb.sistemacursos.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthToken {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; // em milissegundos (86400000 = 24h)

    public String gerarToken(UserDetails usuario){
       return Jwts.builder()
        .subject(usuario.getUsername())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSecretKey())
        .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String extrairLogin(String token) {
        return getClaims(token).getSubject();
    }

    public Date extrairExpiracao(String token) {
        return getClaims(token).getExpiration();
    }

    public Date extrairCriadoEm(String token) {
        return getClaims(token).getIssuedAt();
    }


    public boolean tokenValido(String token, UserDetails user){
        boolean nomeIgual = extrairLogin(token).equals(user.getUsername());
        boolean venceu = extrairExpiracao(token).before(new Date());

        return nomeIgual && !venceu;

    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
