package br.com.remomeurumo.controller;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import br.com.remomeurumo.model.AvaliacaoClinica;
import br.com.remomeurumo.model.InfoClinica;
import br.com.remomeurumo.model.TipoInfoClinica;

/**
 * @author jardim
 */
@RequestScoped
@Path("avaliacaoInfoClinicas")
public class AvaliacaoInfoClinicasController {

	@Inject
	protected EntityManager em;

	@GET
	@Produces(APPLICATION_JSON)
	@Path("procurarAvaliacao")
	public AvaliacaoClinica procurarAvaliacao(@QueryParam("id") Long id) {
		AvaliacaoClinica avaliacaoClinica = em.find(AvaliacaoClinica.class, id);
		// cria as informações clinicas baseadas no modelo da avaliação
		if (avaliacaoClinica.getInformacoesClinicas() == null
				|| avaliacaoClinica.getInformacoesClinicas().isEmpty()) {
			avaliacaoClinica
					.setInformacoesClinicas(new ArrayList<InfoClinica>());
			System.out.println("Criando as informações ");
			if (avaliacaoClinica.getModelo() != null) {
				for (TipoInfoClinica tipoInfo : avaliacaoClinica.getModelo()
						.getTipoInfoClinicas()) {
					InfoClinica infoClinica = new InfoClinica();
					infoClinica.setTipo(tipoInfo);
					infoClinica.setAvaliacao(avaliacaoClinica);
					this.em.persist(infoClinica);
					avaliacaoClinica.getInformacoesClinicas().add(infoClinica);
				}
			}
		}

		return avaliacaoClinica;
	}
}