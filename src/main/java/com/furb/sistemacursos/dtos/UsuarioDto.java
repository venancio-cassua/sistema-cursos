package com.furb.sistemacursos.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.furb.sistemacursos.models.UsuarioModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UsuarioDto {

	private Long id;

	@Email(message = "Login deve ser um email válido")
	private String login;

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	private List<String> role;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$",
		message = "Senha deve ter no mínimo 8 caracteres, uma maiúscula, uma minúscula, um número e um símbolo"
	)
	private String senha;

	public UsuarioDto() {
	}

	public UsuarioDto(UsuarioModel usuario) {
		this.id = usuario.getId();
		this.login = usuario.getLogin();
		this.nome = usuario.getNome();
		this.role = usuario.getRole();
		this.senha = usuario.getSenha();
	}

	public Long getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

}
