package net.barragem.scaffold;

import java.util.List;

import javax.servlet.ServletContext;

import net.barragem.business.bo.EventoBo;
import net.barragem.persistence.entity.Evento;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Treino;
import net.barragem.view.backingbean.componentes.JogadorEventoComparatorVencedorPrimeiro;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class UltimosEventosJob implements Job {

	private static final int MAX_EVENTS_SIZE = 10;

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		doExecute((ServletContext) ctx.getMergedJobDataMap().get("servletContext"));
	}

	public static void doExecute(ServletContext ctx) {
		List<Evento> ultimosEventos = PersistenceHelper.findByNamedQueryWithLimits("ultimosEventosQuery", 0,
		        MAX_EVENTS_SIZE);
		EventoBo bo = new EventoBo(null, null);
		for (Evento ev : ultimosEventos) {
			if (ev instanceof Jogo) {
				bo.inicializaParcial((Jogo) ev, new JogadorEventoComparatorVencedorPrimeiro());
			} else if (ev instanceof Treino) {
				bo.ordenaJogadoresEventos(ev, null);
			}
			if (ev instanceof JogoBarragem) {
				PersistenceHelper.initialize("ranking", ((JogoBarragem) ev).getRodada().getCiclo());
			}
		}

		ctx.setAttribute("ultimosEventos", ultimosEventos);
	}

}
