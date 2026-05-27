package com.furb.sistemacursos.dtos;

import java.time.LocalDate;

import com.furb.sistemacursos.models.MatriculaModel;

import jakarta.validation.constraints.NotNull;

public class MatriculaDto {

	private Long id;

	@NotNull(message = "alunoId é obrigatório")
	private Long alunoId;

	private String alunoNome;

	@NotNull(message = "cursoId é obrigatório")
	private Long cursoId;

	private String cursoNome;

	private LocalDate dataMatricula;

	public MatriculaDto() {
	}

	public MatriculaDto(MatriculaModel matricula) {
		this.id = matricula.getId();
		this.alunoId = matricula.getAluno().getId();
		this.alunoNome = matricula.getAluno().getNome();
		this.cursoId = matricula.getCurso().getId();
		this.cursoNome = matricula.getCurso().getNome();
		this.dataMatricula = matricula.getDataMatricula();
	}

	public Long getId() {
		return id;
	}

	public Long getAlunoId() {
		return alunoId;
	}

	public void setAlunoId(Long alunoId) {
		this.alunoId = alunoId;
	}

	public String getAlunoNome() {
		return alunoNome;
	}

	public Long getCursoId() {
		return cursoId;
	}

	public void setCursoId(Long cursoId) {
		this.cursoId = cursoId;
	}

	public String getCursoNome() {
		return cursoNome;
	}

	public LocalDate getDataMatricula() {
		return dataMatricula;
	}
}
