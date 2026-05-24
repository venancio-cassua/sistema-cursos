package com.furb.sistemacursos.dtos;

import com.furb.sistemacursos.models.AlunoModel;

public class AlunoDto {

	private Long id;

	private String nome;

	private String email;

	public AlunoDto() {
	}

	public AlunoDto(AlunoModel aluno) {
		this.id = aluno.getId();
		this.nome = aluno.getNome();
		this.email = aluno.getEmail();
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
