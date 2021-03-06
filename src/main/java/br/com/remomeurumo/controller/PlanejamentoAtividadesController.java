package br.com.remomeurumo.controller;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.remomeurumo.model.Aluno;
import br.com.remomeurumo.model.Atividade;
import br.com.remomeurumo.model.AtividadeGrupo;
import br.com.remomeurumo.model.Colaborador;
import br.com.remomeurumo.model.Equipamento;
import br.com.remomeurumo.model.GrupoAluno;
import br.com.remomeurumo.model.Tarefa;
import br.com.remomeurumo.model.TipoAtividade;
import br.com.remomeurumo.persistence.Transactional;

/**
 * @author jardim
 */
@RequestScoped
@Transactional
@Path("planejamentoAtividades")
public class PlanejamentoAtividadesController {

	@Inject
	protected EntityManager em;

	@GET
	@Produces(APPLICATION_JSON)
	@Path("procurarGrupos")
	public Atividade procurarGrupos(@QueryParam("id") Long id) {

		Atividade atividade = em.find(Atividade.class, id);
		// se a atividade já tem planejamentos usa os dela, senão procura pelos
		// tipos
		if (atividade.getAtividadeGrupos() == null
				|| atividade.getAtividadeGrupos().isEmpty()) {
			List<GrupoAluno> procurarGrupos = this.procurarGrupos(atividade
					.getTipoAtividade());
			List<AtividadeGrupo> novosGrupos = new ArrayList<AtividadeGrupo>();
			for (GrupoAluno grupo : procurarGrupos) {
				AtividadeGrupo novoPlanejamento = new AtividadeGrupo();
				novoPlanejamento.setAlunos(this.cloneAlunos(grupo.getAlunos()));
				novoPlanejamento.setColaboradores(this.cloneColaboradores(grupo
						.getColaboradores()));
				novoPlanejamento.setGrupo(grupo);
				novoPlanejamento.setAtividade(atividade);
				novosGrupos.add(novoPlanejamento);
				// cria o novo grupo
				this.em.persist(novoPlanejamento);
			}
			atividade.setAtividadeGrupos(novosGrupos);
			this.em.merge(atividade);
		}

		this.loadAlunos(atividade);
		return atividade;
	}

	/**
	 * Método para carregar os alunos e não só o nome e id deles
	 */
	private void loadAlunos(Atividade atividade) {
		atividade.setAtividadeGruposTransient(new ArrayList<AtividadeGrupo>());
		for (AtividadeGrupo atividadeGrupo : atividade.getAtividadeGrupos()) {
			atividade.getAtividadeGruposTransient().add(atividadeGrupo);
			atividadeGrupo.setAlunosTransient(new ArrayList<Aluno>());
			for (Aluno aluno : atividadeGrupo.getAlunos()) {
				atividadeGrupo.getAlunosTransient().add(aluno);
			}
		}
	}

	private List<Aluno> cloneAlunos(Collection<Aluno> alunos) {
		List<Aluno> novosAlunos = new ArrayList<Aluno>();
		for (Aluno aluno : alunos) {
			if (Boolean.TRUE.equals(aluno.getAtivo()))
				novosAlunos.add(aluno);
		}
		return novosAlunos;
	}

	private List<Colaborador> cloneColaboradores(
			Collection<Colaborador> colaboradores) {
		List<Colaborador> novosColaboradores = new ArrayList<Colaborador>();
		for (Colaborador colaborador : colaboradores) {
			if (Boolean.TRUE.equals(colaborador.getAtivo()))
				novosColaboradores.add(colaborador);
		}
		return novosColaboradores;
	}

	@GET
	@SuppressWarnings("unchecked")
	public List<GrupoAluno> procurarGrupos(TipoAtividade tipoAtividade) {

		Session session = (Session) em.getDelegate();
		Criteria criteria = session.createCriteria(GrupoAluno.class);
		criteria.add(Restrictions.eq("tipoAtividade", tipoAtividade));
		criteria.addOrder(Order.desc("id"));
		List<GrupoAluno> list = criteria.list();

		// Precisa verificar se o tipo é um tipo final ou um agrupador
		tipoAtividade = em.find(TipoAtividade.class, tipoAtividade.getId());
		if (tipoAtividade.getTipoAtividadesFilhas() != null
				&& !tipoAtividade.getTipoAtividadesFilhas().isEmpty()) {
			for (TipoAtividade filho : tipoAtividade.getTipoAtividadesFilhas()) {
				list.addAll(procurarGrupos(filho));
			}
		}

		return list;
	}

	@GET
	@Produces(APPLICATION_JSON)
	@Path("removerAluno")
	public Atividade removerAluno(@QueryParam("id") Long id) {
		return em.find(Atividade.class, id);
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Path("salvar")
	public Atividade salvar(Atividade atividade) {

		// deve comparar os grupos que vieram no request contra os que já
		// existiam no banco
		Atividade atividadeOriginal = em.find(Atividade.class,
				atividade.getId());
		Collection<AtividadeGrupo> planejametoMantidos = new ArrayList<AtividadeGrupo>(
				atividade.getAtividadeGrupos());
		Collection<AtividadeGrupo> planejametoRemovidos = new ArrayList<AtividadeGrupo>(
				atividadeOriginal.getAtividadeGrupos());

		planejametoMantidos.retainAll(atividadeOriginal.getAtividadeGrupos());
		planejametoRemovidos.removeAll(atividade.getAtividadeGrupos());

		for (AtividadeGrupo planejamento : planejametoMantidos) {
//			System.out.println("\n\n Merge -- " + planejamento.getId());
//			System.out.println("\n\n Comentario -- "
//					+ planejamento.getComentario());
//			System.out.println("\n\n Alunos -- " + planejamento.getAlunos());
//			System.out.println("\n\n Colaboradores -- "
//					+ planejamento.getColaboradores());
//			System.out.println("\n\n Tarefas -- " + planejamento.getTarefas());
			AtividadeGrupo atividadeGrupo = this.em.merge(planejamento);
			// System.out.println("\n\n Alunos Mantidos -- "+atividadeGrupo.getAlunos().retainAll(planejamento.getAlunos()));
		}
		for (AtividadeGrupo planejamento : planejametoRemovidos) {
			System.out.println("\n\n Removendo -- " + planejamento.getId());
			this.em.remove(planejamento);
		}

		return atividade;
	}

	@GET
	@Path("procurarTarefas")
	@Produces(APPLICATION_JSON)
	public Collection<Tarefa> procurarTarefas(@QueryParam("id") Long id) {
		Atividade atividade = em.find(Atividade.class, id);
		TreeSet<Tarefa> list = new TreeSet<Tarefa>(new TarefaCompare());
		if (atividade.getTipoAtividade() != null) {
			List<GrupoAluno> grupos = this.procurarGrupos(atividade
					.getTipoAtividade());
			for (GrupoAluno grupoAluno : grupos) {
				list.addAll(this.procurarTarefasBytipo(grupoAluno
						.getTipoAtividade()));
			}
		} else {
			list.addAll(this.procurarTarefasBytipo(null));
		}

		return list;
	}

	@GET
	@Path("procurarTarefasM")
	@Produces(APPLICATION_JSON)
	public Map<Long, Collection<Tarefa>> procurarTarefasM(
			@QueryParam("id") Long id) {
		Atividade atividade = em.find(Atividade.class, id);
		Map<Long, Collection<Tarefa>> tarefasM = new HashMap<Long, Collection<Tarefa>>();
		if (atividade.getTipoAtividade() != null) {
			List<GrupoAluno> grupos = this.procurarGrupos(atividade
					.getTipoAtividade());
			for (GrupoAluno grupoAluno : grupos) {
				tarefasM.put(grupoAluno.getId(), this
						.procurarTarefasBytipo(grupoAluno.getTipoAtividade()));
			}
		}
		return tarefasM;
	}

	private Collection<Tarefa> procurarTarefasBytipo(TipoAtividade tipo) {
		Session session = (Session) em.getDelegate();
		Criteria criteria = session.createCriteria(Tarefa.class);
		if (tipo != null)
			criteria.add(Restrictions.eq("tipoAtividade", tipo));
		criteria.add(Restrictions.isNotNull("tarefaPai"));

		TreeSet<Tarefa> order = new TreeSet<Tarefa>(new TarefaCompare());
		order.addAll(criteria.list());
		
		return order;
	}

	@GET
	@Path("procurarEquipamentos")
	@Produces(APPLICATION_JSON)
	public List<Equipamento> procurarEquipamentos() {
		Session session = (Session) em.getDelegate();
		Criteria criteria = session.createCriteria(Equipamento.class);
		criteria.addOrder(Order.asc("nome"));
		List<Equipamento> list = criteria.list();
		return list;
	}

	class TarefaCompare implements Comparator<Tarefa> {
		@Override
		public int compare(Tarefa o1, Tarefa o2) {
			return o1.getNomeCompleto().compareTo(o2.getNomeCompleto());
		}
	}

}
