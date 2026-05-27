package com.furb.sistemacursos.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.furb.sistemacursos.dtos.UsuarioDto;
import com.furb.sistemacursos.exception.RecursoJaExistenteException;
import com.furb.sistemacursos.models.UsuarioModel;
import com.furb.sistemacursos.repository.UsuarioRepository;
import com.furb.sistemacursos.services.UsuarioService;

@Component
public class DataIntializer implements ApplicationRunner{

    private UsuarioService usuarioService;
    private PasswordEncoder passwordEncoder;
    
    public DataIntializer(UsuarioService usuarioService, PasswordEncoder passwordEncoder){
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UsuarioModel user = new UsuarioModel();

        user.setLogin("admin");
        user.setNome("admin");
        user.setSenha("admin");
        user.setRole(new ArrayList<>(List.of("master")));

        try {
            this.usuarioService.cadastrarUsuario( new UsuarioDto(user));
            System.out.println("Admin criado!");
            
        } catch (RecursoJaExistenteException e) {
            System.out.println("Admin ja existe!");
        }

    }

}
