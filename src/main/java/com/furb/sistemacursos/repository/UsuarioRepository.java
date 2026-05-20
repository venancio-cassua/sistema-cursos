package com.furb.sistemacursos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.furb.sistemacursos.models.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

	Optional<UsuarioModel> findByLogin(String login);

}
