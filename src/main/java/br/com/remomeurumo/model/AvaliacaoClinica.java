package br.com.remomeurumo.model;

import br.com.remomeurumo.framework.BaseEntity;

import java.util.Collection;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * @author jardim
 */
@Entity
public class AvaliacaoClinica extends BaseEntity {

	private String data;

	private String comentario;
	
	//TODO: ANexo a ficha clinica

	@JsonFilter("associationFilter")
	@ManyToOne
	@JoinColumn(name = "modeloid")
	private ModeloAvaliacaoClinica modelo;
	
	@JsonFilter("associationFilter")
	@ManyToOne
	@JoinColumn(name = "alunoid")
	private Aluno aluno;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	
	public ModeloAvaliacaoClinica getModelo() {
		return modelo;
	}

	public void setModelo(ModeloAvaliacaoClinica modelo) {
		this.modelo = modelo;
	}



	private static final long serialVersionUID = 1L;
}