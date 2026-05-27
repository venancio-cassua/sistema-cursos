package com.furb.sistemacursos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.furb.sistemacursos.dtos.MatriculaDto;
import com.furb.sistemacursos.exception.RecursoJaExistenteException;
import com.furb.sistemacursos.exception.RecursoNaoEncontradoException;
import com.furb.sistemacursos.models.AlunoModel;
import com.furb.sistemacursos.models.CursoModel;
import com.furb.sistemacursos.models.MatriculaModel;
import com.furb.sistemacursos.repository.AlunoRepository;
import com.furb.sistemacursos.repository.CursoRepository;
import com.furb.sistemacursos.repository.MatriculaRepository;

@Service
public class MatriculaService {

	private final MatriculaRepository matriculaRepository;
	private final AlunoRepository alunoRepository;
	private final CursoRepository cursoRepository;

	public MatriculaService(MatriculaRepository matriculaRepository,
			AlunoRepository alunoRepository, CursoRepository cursoRepository) {
		this.matriculaRepository = matriculaRepository;
		this.alunoRepository = alunoRepository;
		this.cursoRepository = cursoRepository;
	}

	public List<MatriculaDto> listar() {
		return matriculaRepository.findAll().stream()
				.map(MatriculaDto::new)
				.toList();
	}

	public MatriculaDto buscarPorId(Long id) {
		MatriculaModel matricula = matriculaRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada com id: " + id));
		return new MatriculaDto(matricula);
	}

	public MatriculaDto matricular(MatriculaDto dto) {
		AlunoModel aluno = alunoRepository.findById(dto.getAlunoId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com id: " + dto.getAlunoId()));

		CursoModel curso = cursoRepository.findById(dto.getCursoId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado com id: " + dto.getCursoId()));

		if (matriculaRepository.existsByAlunoIdAndCursoId(dto.getAlunoId(), dto.getCursoId())) {
			throw new RecursoJaExistenteException("Aluno já matriculado neste curso");
		}

		MatriculaModel matricula = new MatriculaModel();
		matricula.setAluno(aluno);
		matricula.setCurso(curso);

		return new MatriculaDto(matriculaRepository.save(matricula));
	}

	public void cancelar(Long id) {
		MatriculaModel matricula = matriculaRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada com id: " + id));
		matriculaRepository.delete(matricula);
	}
}
