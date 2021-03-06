package br.com.remomeurumo.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.com.remomeurumo.framework.BaseEntity;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * @author jardim
 */
@Entity
public class AvaliacaoClinica extends BaseEntity {

	private String data;

	@Column(name= "comentario", length=2048)
	private String comentario;

	private Boolean fechada;

	// TODO: ANexo a ficha clinica

	@JsonFilter("associationFilter")
	@ManyToOne
	@JoinColumn(name = "modeloid")
	private ModeloAvaliacaoClinica modelo;

	@JsonFilter("associationFilter")
	@ManyToOne
	@JoinColumn(name = "alunoid")
	private Aluno aluno;

	@OneToMany(mappedBy = "avaliacao")
	private Collection<InfoClinica> informacoesClinicas;

	@Transient
	public String getDataFormatada() {
		if(this.data!=null && this.data.indexOf("T")> 0){
			//2016-05-04T15:00:00.000Z
			return this.data.substring(0, this.data.indexOf("T"));
		}
		return "";
	}
	
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

	public Collection<InfoClinica> getInformacoesClinicas() {
		return informacoesClinicas;
	}

	public void setInformacoesClinicas(
			Collection<InfoClinica> informacoesClinicas) {
		this.informacoesClinicas = informacoesClinicas;
	}

	public Boolean getFechada() {
		return fechada;
	}

	public void setFechada(Boolean fechada) {
		this.fechada = fechada;
	}

	@Transient
	public Object[] csvHead() {

		ArrayList<String> returnString = new ArrayList<String>();

		returnString.add("id");
		returnString.add("Data");
		returnString.add("Comentario");
		returnString.add("Fechada");

		return returnString.toArray();
	}

	@Transient
	public Object[] csv() {

		ArrayList<String> returnString = new ArrayList<String>();

		returnString.add(String.valueOf(this.getId()));
		returnString.add(this.getData());
		returnString.add(this.getComentario());
		returnString.add(String.valueOf(this.getFechada()));

		return returnString.toArray();
	}

	private static final long serialVersionUID = 1L;
}
