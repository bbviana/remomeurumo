package br.com.remomeurumo.model;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFilter;

import br.com.remomeurumo.framework.BaseEntity;

/**
 * @author jardim
 */
@Entity
public class TipoInfoClinica extends BaseEntity {

	private String nome;

	private String sigla;

	@JsonFilter("associationFilter")
	@ManyToOne
	@JoinColumn(name = "especialidadeid")
	private EspecialidadeClinica especialidade;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public EspecialidadeClinica getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(EspecialidadeClinica especialidade) {
		this.especialidade = especialidade;
	}

	@Transient
	public Object[] csvHead() {
		
		ArrayList<String> returnString = new ArrayList<String>();
		
		returnString.add("id");
		returnString.add("Nome");
		returnString.add("Sigla");
				
		return returnString.toArray();
	}

	@Transient
	public Object[] csv() {

		ArrayList<String> returnString = new ArrayList<String>();

		returnString.add(String.valueOf(this.getId()));
		returnString.add(this.getNome());
		returnString.add(this.getSigla());
		

		return returnString.toArray();
	}
	
	private static final long serialVersionUID = 1L;
}
