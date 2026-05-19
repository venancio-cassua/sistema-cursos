package com.furb.sistemacursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.furb.sistemacursos.models.CursoModel;

public interface CursoRepository extends JpaRepository<CursoModel, Long> {

}
