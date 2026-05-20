package com.furb.sistemacursos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.furb.sistemacursos.dtos.CursoDto;
import com.furb.sistemacursos.models.CursoModel;
import com.furb.sistemacursos.repository.CursoRepository;

@Service
public class CursoService {

	private final CursoRepository cursoRepository;

	public CursoService(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	public List<CursoDto> listarCursos() {

		List<CursoDto> cursos = new ArrayList<>();

		for (CursoModel curso : this.cursoRepository.findAll()) {
			cursos.add(new CursoDto(curso));
		}

		return cursos;
	}

	public CursoDto buscarCursoId(Long id) {
		Optional<CursoModel> cursoOptional = this.cursoRepository.findById(id);

		if (cursoOptional.isEmpty()) {
			return null;
		}
		CursoDto curso = new CursoDto(cursoOptional.get());

		return curso;
	}

	public CursoDto cadastrarCurso(CursoDto cursoDto) {
		Optional<CursoModel> cursoOptional = this.cursoRepository.findByNome(cursoDto.getNome());

		if (cursoOptional.isPresent()) {
			// Curso ja esta cadastrado
			return null;
		}

		CursoModel curso = new CursoModel();
		curso.setNome(cursoDto.getNome());
		curso.setDescricao(cursoDto.getDescricao());
		curso.setCargaHoraria(cursoDto.getCargaHoraria());

		return new CursoDto(this.cursoRepository.save(curso));
	}
	
	public CursoDto atualizarCurso(Long id, CursoDto cursoDto) {
		Optional<CursoModel> cursoOptional = this.cursoRepository.findById(id);
		
		if(cursoOptional.isEmpty()) {
			return null;
		}
		
		CursoModel curso = cursoOptional.get();
		curso.setNome(cursoDto.getNome());
		curso.setDescricao(cursoDto.getDescricao());
		curso.setCargaHoraria(cursoDto.getCargaHoraria());
		
		return new CursoDto(this.cursoRepository.save(curso));
	}
	
	public void detetarCurso(Long id) {
		Optional<CursoModel> cursoOptional = this.cursoRepository.findById(id);
		if(cursoOptional.isEmpty()) {
			// Usuario nao existe
			return ;
		}
		
		this.cursoRepository.delete(cursoOptional.get());
	}
}
