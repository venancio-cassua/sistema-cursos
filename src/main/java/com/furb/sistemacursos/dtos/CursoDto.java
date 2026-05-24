package com.furb.sistemacursos.dtos;

import com.furb.sistemacursos.models.CursoModel;

public class CursoDto {

	private Long id;

	private String nome;

	private String descricao;

	private Integer cargaHoraria;

	public CursoDto() {
	}

	public CursoDto(CursoModel curso) {
		this.id = curso.getId();
		this.nome = curso.getNome();
		this.descricao = curso.getDescricao();
		this.cargaHoraria = curso.getCargaHoraria();
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
