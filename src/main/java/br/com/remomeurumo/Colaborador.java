package br.com.remomeurumo;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import br.com.remomeurumo.tipo.TipoColaborador;

/**
 * @author jardim
 */
@Entity
public class Colaborador extends Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private TipoColaborador tipoColcaborador;

	@ManyToMany(targetEntity = Atividade.class)
	@JoinTable(name = "ColaboradorAtividade")
	private Collection<Atividade> atividades;

	@Override
	public String toString() {
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

	public TipoColaborador getTipoColcaborador() {
		return tipoColcaborador;
	}

	public void setTipoColcaborador(TipoColaborador tipoColcaborador) {
		this.tipoColcaborador = tipoColcaborador;
	}

	public Collection<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividade(Collection<Atividade> atividades) {
		this.atividades = atividades;
	}

}