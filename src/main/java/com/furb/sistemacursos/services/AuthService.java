package com.furb.sistemacursos.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.furb.sistemacursos.repository.UsuarioRepository;

@Service
public class AuthService implements  UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUsuarioRepository()
        .findByLogin(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("Usuário não encontrado: " + username)
        );
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }


    
}
