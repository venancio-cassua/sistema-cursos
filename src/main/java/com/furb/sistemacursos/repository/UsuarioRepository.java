package com.furb.sistemacursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.furb.sistemacursos.models.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

}
