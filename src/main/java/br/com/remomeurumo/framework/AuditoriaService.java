/*
 * Copyright (c) 1999-2010 Touch Tecnologia e Informatica Ltda.
 *
 * R. Gomes de Carvalho, 1666, 3o. Andar, Vila Olimpia, Sao Paulo, SP, Brasil.
 *
 * Todos os direitos reservados.
 * Este software e confidencial e de propriedade da Touch Tecnologia e Informatica Ltda. (Informacao Confidencial)
 * As informacoes contidas neste arquivo nao podem ser publicadas, e seu uso esta limitado de acordo
 * com os termos do contrato de licenca.
 */
package br.com.remomeurumo.framework;

import static org.hibernate.criterion.MatchMode.ANYWHERE;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.remomeurumo.model.Auditavel;
import br.com.remomeurumo.model.RegistroAuditoria;
import br.com.remomeurumo.model.Usuario;

/**
 * @author jardim
 */
public class AuditoriaService {

	public static String operationUpdate = "ATUALIZAR";
	public static String operationSave = "CRIAR";
	public static String operationDelete = "REMOVER";

	@Inject
	protected EntityManager em;

	public void registrarAuditoria(Auditavel entidade, String tipoOperacao,
			HttpServletRequest request) {

		RegistroAuditoria registro = new RegistroAuditoria();
		registro.setNome(entidade.getClass().getSimpleName());
		registro.setIdEntidade(entidade.getId());
		registro.setRegistro(entidade.auditoria());
		registro.setTipoOperacao(tipoOperacao);
		registro.setUsuario(getUsuarioLogado(request));
		GregorianCalendar dataRegistro = new GregorianCalendar();
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MMM/yyyy-HH:mm");
		registro.setDataRegistro(fmt.format(dataRegistro.getTime()));
		this.em.persist(registro);
	}

	private Usuario getUsuarioLogado(HttpServletRequest request) {

		if(request.getUserPrincipal()!=null) {
			String nome = request.getUserPrincipal().getName();
			if (nome != null) {
				Session session = (Session) em.getDelegate();
				Criteria criteria = session.createCriteria(Usuario.class);
				criteria.add(Restrictions.ilike("nome", nome, ANYWHERE));
	
				List<Usuario> list = criteria.list();
				if (list != null && !list.isEmpty())
					return list.iterator().next();
			}
		}

		return null;
	}
}
