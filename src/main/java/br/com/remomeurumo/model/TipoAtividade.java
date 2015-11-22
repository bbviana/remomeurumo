package br.com.remomeurumo.model;

import br.com.remomeurumo.framework.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;

/**
 * @author jardim
 */
@Entity
public class TipoAtividade extends BaseEntity {

	private String nome;

	@ManyToMany(mappedBy = "tipoAtividade")
	private Collection<Atividade> atividades;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Collection<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(Collection<Atividade> atividades) {
		this.atividades = atividades;
	}

	private static final long serialVersionUID = 1L;
}