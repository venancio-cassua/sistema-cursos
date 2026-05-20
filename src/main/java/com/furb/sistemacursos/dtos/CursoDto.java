package com.furb.sistemacursos.dtos;

import com.furb.sistemacursos.models.CursoModel;

public class CursoDto {
	
	private String nome;
	
	private String descricao;
	
	private Integer cargaHoraria;
	
	public CursoDto() {
	}
	
	public CursoDto(CursoModel curso) {
		this.setNome(curso.getNome());
		this.setDescricao(curso.getDescricao());
		this.setCargaHoraria(curso.getCargaHoraria());
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

}
