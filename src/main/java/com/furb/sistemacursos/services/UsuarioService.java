package com.furb.sistemacursos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.furb.sistemacursos.dtos.UsuarioDto;
import com.furb.sistemacursos.models.UsuarioModel;
import com.furb.sistemacursos.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public List<UsuarioDto> listarUsuarios() {
		List<UsuarioDto> usuarios = new ArrayList<>();

		for (UsuarioModel usuario : this.usuarioRepository.findAll()) {
			usuarios.add(new UsuarioDto(usuario));
		}

		return usuarios;
	}

	public UsuarioDto buscarUsuarioId(Long id) {
		Optional<UsuarioModel> usuarioOptional = this.usuarioRepository.findById(id);

		if (usuarioOptional.isEmpty()) {
			return null;
		}

		return new UsuarioDto(usuarioOptional.get());
	}

	public UsuarioDto cadastrarUsuario(UsuarioDto usuarioDto) {
		Optional<UsuarioModel> usuarioOptional = this.usuarioRepository.findByLogin(usuarioDto.getLogin());

		if (usuarioOptional.isPresent()) {
			return null;
		}

		UsuarioModel usuario = new UsuarioModel();
		usuario.setLogin(usuarioDto.getLogin());
		usuario.setSenha(usuarioDto.getSenha());

		return new UsuarioDto(this.usuarioRepository.save(usuario));
	}

}
