package com.furb.sistemacursos.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_matricula")
public class MatriculaModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "aluno_id", nullable = false)
	private AlunoModel aluno;

	@ManyToOne
	@JoinColumn(name = "curso_id", nullable = false)
	private CursoModel curso;

	@Column(name = "data_matricula", nullable = false)
	private LocalDate dataMatricula;

	@PrePersist
	private void prePersist() {
		this.dataMatricula = LocalDate.now();
	}

	public MatriculaModel() {
	}

	public Long getId() {
		return id;
	}

	public AlunoModel getAluno() {
		return aluno;
	}

	public void setAluno(AlunoModel aluno) {
		this.aluno = aluno;
	}

	public CursoModel getCurso() {
		return curso;
	}

	public void setCurso(CursoModel curso) {
		this.curso = curso;
	}

	public LocalDate getDataMatricula() {
		return dataMatricula;
	}
}
