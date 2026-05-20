package com.furb.sistemacursos.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furb.sistemacursos.dtos.CursoDto;
import com.furb.sistemacursos.services.CursoService;

@RestController
@RequestMapping("/curso")
public class CursoController {

	private final CursoService cursoService;

	public CursoController(CursoService cursoService) {
		this.cursoService = cursoService;
	} 

	@GetMapping()
	public List<CursoDto> listaCurso() {

		return this.cursoService.listarCursos();
	}

	@GetMapping("/{id}")
	public CursoDto buscaCursoId(@PathVariable Long id) {

		return this.cursoService.buscarCursoId(id);
	}

	@PostMapping()
	public CursoDto cadastrarCurso(@RequestBody CursoDto curso) {
		return this.cursoService.cadastrarCurso(curso);
	}

	@PutMapping("/{id}")
	public CursoDto atualizarCurso(@PathVariable Long id, @RequestBody CursoDto curso) {
		return this.cursoService.atualizarCurso(id, curso);
	}

	@DeleteMapping("/{id}")
	public void detetarCurso(@PathVariable Long id) {
		this.cursoService.detetarCurso(id);
	}
}
