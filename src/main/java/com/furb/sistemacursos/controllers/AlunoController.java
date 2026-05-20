package com.furb.sistemacursos.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public List<AlunoDto> listaAluno(){
		
		return this.alunoService.listaAluno(); 
	}

	@GetMapping("/{id}")
	public AlunoDto mostrarAlunoId(@PathVariable Long id) {
		return alunoService.mostrarAlunoId(id); 
	}
}
