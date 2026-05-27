package com.furb.sistemacursos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.furb.sistemacursos.dtos.AlunoDto;
import com.furb.sistemacursos.services.AlunoService;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

	private final AlunoService alunoService;

	public AlunoController(AlunoService alunoService) {
		this.alunoService = alunoService;
	}

	@GetMapping()
	public ResponseEntity<List<AlunoDto>> listaAluno() {

		//return this.alunoService.listaAluno();
		
		return ResponseEntity.status(HttpStatus.OK).body(this.alunoService.listaAluno());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlunoDto> mostrarAlunoId(@PathVariable Long id) {
		//return alunoService.mostrarAlunoId(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(this.alunoService.mostrarAlunoId(id));
	}

	@PostMapping()
	public ResponseEntity<AlunoDto> cadastrarAlunos(@Valid @RequestBody AlunoDto aluno) {
		//return this.alunoService.cadastrarAlunos(aluno);
		return ResponseEntity.status(HttpStatus.CREATED).body(this.alunoService.cadastrarAlunos(aluno));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AlunoDto> atualizarAluno(@PathVariable Long id, @Valid @RequestBody AlunoDto alunoDto) {
		//return this.alunoService.atualizarAluno(id, alunoDto);
		return ResponseEntity.status(HttpStatus.OK).body(this.alunoService.atualizarAluno(id, alunoDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirAluno(@PathVariable Long id) {
		this.alunoService.deletarAluno(id);
		
		return ResponseEntity.noContent().build();
		
	}
}
