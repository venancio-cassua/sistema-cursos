package com.furb.sistemacursos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.furb.sistemacursos.models.AlunoModel;

public interface AlunoRepository extends JpaRepository<AlunoModel, Long> {
	Optional<AlunoModel> findByEmail(String email);

}
