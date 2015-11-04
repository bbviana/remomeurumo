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
package br.com.remomeurumo.controller;

import br.com.remomeurumo.BaseEntity;
import br.com.remomeurumo.persistence.Transactional;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hibernate.criterion.MatchMode.ANYWHERE;

/**
 * @author bbviana
 */
@Transactional
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class CrudController<T extends BaseEntity> {

	@Inject
	protected EntityManager em;

	protected Class<T> getType() {
		return null;
	}

	@POST
	public T insert(T element) {
		em.persist(element);
		return element;
	}

	@PUT
	@Path("{id}")
	public T update(@PathParam("id") Long id, T element) {
		element.setId(id);
		return em.merge(element);
	}

	@GET
	@Path("{id}")
	public T get(@PathParam("id") Long id) {
		return em.find(getType(), id);
	}

	@GET
	@SuppressWarnings("unchecked")
	public ResultList<T> list(
			@QueryParam("count") Integer count,
			@QueryParam("page") Integer page,
			@QueryParam("search.nome") String nome) {


		Session session = (Session) em.getDelegate();
		Criteria criteria = session.createCriteria(getType());
		Criteria countCriteria = session.createCriteria(getType());

		criteria.addOrder(Order.asc("nome"));

		if (count != null) {
			criteria.setMaxResults(count);
		}

		if (count != null && page != null) {
			criteria.setFirstResult((page - 1) * count);
		}

		if (nome != null) {
			criteria.add(Restrictions.ilike("nome", nome, ANYWHERE));
			countCriteria.add(Restrictions.ilike("nome", nome, ANYWHERE));
		}

		List<T> list = criteria.list();
		Long totalResults = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();

		return new ResultList<>(list, count, totalResults.intValue());
	}

	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") Long id) {
		T entity = em.find(getType(), id);
		em.remove(entity);
	}
}