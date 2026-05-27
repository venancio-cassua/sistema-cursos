package com.furb.sistemacursos.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.furb.sistemacursos.dtos.UsuarioDto;
import com.furb.sistemacursos.exception.RecursoJaExistenteException;
import com.furb.sistemacursos.exception.RecursoNaoEncontradoException;
import com.furb.sistemacursos.models.UsuarioModel;
import com.furb.sistemacursos.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
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

		List<String> roles = (usuarioDto.getRole() != null && !usuarioDto.getRole().isEmpty())
				? usuarioDto.getRole()
				: new ArrayList<>(List.of("ROLE_USER"));

		UsuarioModel usuario = new UsuarioModel();
		usuario.setLogin(usuarioDto.getLogin());
		usuario.setNome(usuarioDto.getNome());
		usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
		usuario.setRole(roles);

		return new UsuarioDto(this.usuarioRepository.save(usuario));
	}

	public void deletarUsuario(Long id) {
		UsuarioModel usuario = this.usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id:" + id));

		this.usuarioRepository.delete(usuario);
	}

}
