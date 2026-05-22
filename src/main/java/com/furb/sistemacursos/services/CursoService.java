package com.furb.sistemacursos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.furb.sistemacursos.dtos.CursoDto;
import com.furb.sistemacursos.exception.RecursoJaExistenteException;
import com.furb.sistemacursos.exception.RecursoNaoEncontradoException;
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

		CursoModel curso = this.cursoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado com id:" + id));

		return new CursoDto(curso);
	}

	public CursoDto cadastrarCurso(CursoDto cursoDto) {
		if (this.cursoRepository.existsByNome(cursoDto.getNome())) {
			throw new RecursoJaExistenteException("Curso já cadastrado:" + cursoDto.getNome());
		}

		CursoModel curso = new CursoModel();
		curso.setNome(cursoDto.getNome());
		curso.setDescricao(cursoDto.getDescricao());
		curso.setCargaHoraria(cursoDto.getCargaHoraria());

		return new CursoDto(this.cursoRepository.save(curso));
	}

	public CursoDto atualizarCurso(Long id, CursoDto cursoDto) {
		Optional<CursoModel> cursoOptional = this.cursoRepository.findById(id);

		CursoModel curso = this.cursoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado com id:" + id));

		curso.setNome(cursoDto.getNome());
		curso.setDescricao(cursoDto.getDescricao());
		curso.setCargaHoraria(cursoDto.getCargaHoraria());

		return new CursoDto(this.cursoRepository.save(curso));
	}

	public void detetarCurso(Long id) {

		CursoModel curso = this.cursoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado com id:" + id));

		this.cursoRepository.delete(curso);
	}
}
