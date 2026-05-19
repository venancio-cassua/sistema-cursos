package com.furb.sistemacursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.furb.sistemacursos.models.AlunoModel;

public interface AlunoRepository extends JpaRepository<AlunoModel, Long> {

}
