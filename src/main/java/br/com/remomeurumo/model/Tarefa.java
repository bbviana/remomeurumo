package br.com.remomeurumo.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFilter;

import br.com.remomeurumo.framework.AuditoriaService;
import br.com.remomeurumo.framework.BaseEntity;

/**
 * @author jardim
 */
@Entity
@EntityListeners(AuditoriaService.class)
public class Tarefa extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;

	private String descricao;

	@JsonFilter("associationFilter")
	@ManyToOne
	@JoinColumn(name = "tipoid")
	private TipoAtividade tipoAtividade;
	
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
	
	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	@Transient
	public Object[] csvHead() {

		ArrayList<String> returnString = new ArrayList<String>();

		returnString.add("id");
		returnString.add("Nome");
		returnString.add("Descricao");

		return returnString.toArray();
	}

	@Transient
	public Object[] csv() {

		ArrayList<String> returnString = new ArrayList<String>();

		returnString.add(String.valueOf(this.getId()));
		returnString.add(this.getNome());
		returnString.add(this.getDescricao());

		return returnString.toArray();
	}

}