package com.furb.sistemacursos.dtos;

import com.furb.sistemacursos.models.AlunoModel;

public class AlunoDto {

	private String nome;

	private String email;

	public AlunoDto() {
	}

	public AlunoDto(AlunoModel aluno) {
		this.setNome(aluno.getNome());
		this.setEmail(aluno.getEmail());
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
