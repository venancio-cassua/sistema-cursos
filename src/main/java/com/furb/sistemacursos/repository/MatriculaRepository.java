package com.furb.sistemacursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.furb.sistemacursos.models.MatriculaModel;

public interface MatriculaRepository extends JpaRepository<MatriculaModel, Long> {
	boolean existsByAlunoIdAndCursoId(Long alunoId, Long cursoId);
	List<MatriculaModel> findByAlunoId(Long alunoId);
	List<MatriculaModel> findByCursoId(Long cursoId);
}
