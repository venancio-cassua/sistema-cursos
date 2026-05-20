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
}
