package com.furb.sistemacursos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.furb.sistemacursos.dtos.AlunoDto;
import com.furb.sistemacursos.exception.RecursoJaExistenteException;
import com.furb.sistemacursos.exception.RecursoNaoEncontradoException;
import com.furb.sistemacursos.models.AlunoModel;
import com.furb.sistemacursos.repository.AlunoRepository;

@Service
public class AlunoService {

	private final AlunoRepository alunoRepository;

	public AlunoService(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	public List<AlunoDto> listaAluno() {
		List<AlunoDto> alunos = new ArrayList<>();

		for (AlunoModel aluno : alunoRepository.findAll()) {
			alunos.add(new AlunoDto(aluno));
		}

		return alunos;
	}

	public AlunoDto mostrarAlunoId(Long id) {
		AlunoModel aluno = this.alunoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com id:" + id));

		return new AlunoDto(aluno);
	}

	public AlunoDto cadastrarAlunos(AlunoDto aluno) {

		if (this.alunoRepository.existsByEmail(aluno.getEmail())) {
			// Aluno já está cadastrado
			throw new RecursoJaExistenteException("Aluno já cadastrado: " + aluno.getEmail());
		}

		AlunoModel alunoModel = new AlunoModel();
		alunoModel.setNome(aluno.getNome());
		alunoModel.setEmail(aluno.getEmail());

		return new AlunoDto(this.alunoRepository.save(alunoModel));
	}

	public AlunoDto atualizarAluno(Long id, AlunoDto alunoDto) {
		AlunoModel aluno = this.alunoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com id:" + id));

		aluno.setNome(alunoDto.getNome());
		aluno.setEmail(alunoDto.getEmail());

		return new AlunoDto(this.alunoRepository.save(aluno));
	}

	public void deletarAluno(Long id) {
		AlunoModel aluno = this.alunoRepository.findById(id).orElseThrow(
				()-> new RecursoNaoEncontradoException("Aluno não encontrado com id:" + id));

		this.alunoRepository.delete(aluno);
	}
}
