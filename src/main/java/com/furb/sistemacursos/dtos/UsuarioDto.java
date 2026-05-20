package com.furb.sistemacursos.dtos;

import com.furb.sistemacursos.models.UsuarioModel;

public class UsuarioDto {

	private String login;

	private String senha;

	public UsuarioDto() {
	}

	public UsuarioDto(UsuarioModel usuario) {
		this.login = usuario.getLogin();
		this.senha = usuario.getSenha();
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

}
