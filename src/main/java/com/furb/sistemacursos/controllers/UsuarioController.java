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

import com.furb.sistemacursos.dtos.UsuarioDto;
import com.furb.sistemacursos.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping()
	public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
		return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.listarUsuarios());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> buscarUsuarioId(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.buscarUsuarioId(id));
	}

	@PostMapping()
	public ResponseEntity<UsuarioDto> cadastrarUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.usuarioService.cadastrarUsuario(usuarioDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
		this.usuarioService.deletarUsuario(id);
		return ResponseEntity.noContent().build();
	}

}
