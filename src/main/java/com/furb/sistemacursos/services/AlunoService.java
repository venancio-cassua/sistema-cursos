package com.furb.sistemacursos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.furb.sistemacursos.dtos.AlunoDto;
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
		Optional<AlunoModel> alunoOptional = alunoRepository.findById(id);

		if (alunoOptional.isEmpty()) {
			return null;
		}

		AlunoDto aluno = new AlunoDto(alunoOptional.get());

		return aluno;
	}

	public AlunoDto cadastrarAlunos(AlunoDto aluno) {
		Optional<AlunoModel> alunoOptional = this.alunoRepository.findByEmail(aluno.getEmail());

		if (alunoOptional.isPresent()) {
			// Aluno já está cadastrado
			return null;
		}
		AlunoModel alunoModel = new AlunoModel();
		alunoModel.setNome(aluno.getNome());
		alunoModel.setEmail(aluno.getEmail());

		return new AlunoDto(this.alunoRepository.save(alunoModel));
	}

	public AlunoDto atualizarAluno(Long id, AlunoDto alunoDto) {
		Optional<AlunoModel> alunoOptional = this.alunoRepository.findById(id);

		if (alunoOptional.isEmpty()) {
			// aluno não existe na base de dados, não tem como atualizar algo que não
			// existe.
			return null;
		}

		AlunoModel aluno = alunoOptional.get();
		aluno.setNome(alunoDto.getNome());
		aluno.setEmail(alunoDto.getEmail());

		return new AlunoDto(this.alunoRepository.save(aluno));
	}

	public void deletarAluno(Long id) {
		Optional<AlunoModel> alunoOptional = this.alunoRepository.findById(id);

		if (alunoOptional.isEmpty()) {
			System.out.println("Aluno não existente.");
			return;
		}

		this.alunoRepository.delete(alunoOptional.get());
	}
}
