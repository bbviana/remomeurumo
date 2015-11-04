package br.com.remomeurumo;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

/**
 * @author jardim
 */
@Entity
public class Responsavel extends Pessoa {

	@ManyToMany(targetEntity = Aluno.class, mappedBy = "responsaveis")
	private Collection<Aluno> alunos;

	@Override
	public String toString() {
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

	public Collection<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(Collection<Aluno> alunos) {
		this.alunos = alunos;
	}
}
