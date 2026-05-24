package com.furb.sistemacursos.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.furb.sistemacursos.dtos.UsuarioDto;
import com.furb.sistemacursos.exception.RecursoJaExistenteException;
import com.furb.sistemacursos.exception.RecursoNaoEncontradoException;
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
		UsuarioModel usuario = this.usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id:" + id));

		return new UsuarioDto(usuario);
	}

	public UsuarioDto cadastrarUsuario(UsuarioDto usuarioDto) {
		if (this.usuarioRepository.findByLogin(usuarioDto.getLogin()).isPresent()) {
			throw new RecursoJaExistenteException("Usuário já cadastrado: " + usuarioDto.getLogin());
		}

		UsuarioModel usuario = new UsuarioModel();
		usuario.setLogin(usuarioDto.getLogin());
		usuario.setSenha(usuarioDto.getSenha());

		return new UsuarioDto(this.usuarioRepository.save(usuario));
	}

	public void deletarUsuario(Long id) {
		UsuarioModel usuario = this.usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id:" + id));

		this.usuarioRepository.delete(usuario);
	}

}
