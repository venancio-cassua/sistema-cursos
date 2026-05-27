package com.furb.sistemacursos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.furb.sistemacursos.dtos.MatriculaDto;
import com.furb.sistemacursos.services.MatriculaService;

@RestController
@RequestMapping("/matricula")
public class MatriculaController {

	private final MatriculaService matriculaService;

	public MatriculaController(MatriculaService matriculaService) {
		this.matriculaService = matriculaService;
	}

	@GetMapping
	public ResponseEntity<List<MatriculaDto>> listar() {
		return ResponseEntity.ok(matriculaService.listar());
	}

	@GetMapping("/{id}")
	public ResponseEntity<MatriculaDto> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(matriculaService.buscarPorId(id));
	}

	@PostMapping
	public ResponseEntity<MatriculaDto> matricular(@Valid @RequestBody MatriculaDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.matricular(dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelar(@PathVariable Long id) {
		matriculaService.cancelar(id);
		return ResponseEntity.noContent().build();
	}
}
