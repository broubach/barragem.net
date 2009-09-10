package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.JogadorJogo;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Rodada;
import net.barragem.util.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GerirRodadaBean extends BaseBean {

	private Rodada rodadaEmFoco;

	public Rodada getRodadaEmFoco() {
		return rodadaEmFoco;
	}

	public void setRodadaEmFoco(Rodada rodadaEmFoco) {
		this.rodadaEmFoco = rodadaEmFoco;
	}

	public void editaJogo(ActionEvent e) {
		GerirJogoBarragemBean jogoBarragemBean = new GerirJogoBarragemBean();
		jogoBarragemBean.carregaJogo(rodadaEmFoco, getIndex());

		setRequestAttribute("gerirJogoBarragemBean", jogoBarragemBean);
	}

	public void adicionaJogo(ActionEvent e) {
		GerirJogoBarragemBean jogoBarragemBean = new GerirJogoBarragemBean();
		jogoBarragemBean.adicionaJogo(rodadaEmFoco);

		setRequestAttribute("gerirJogoBarragemBean", jogoBarragemBean);
	}

	public void removeJogo(ActionEvent e) {
		PersistenceHelper.remove(rodadaEmFoco.getJogos().remove(getIndex()));
	}

	public void salvaRodada(ActionEvent e) {
		PersistenceHelper.persiste(rodadaEmFoco);
		Collections.sort(rodadaEmFoco.getJogos(), new JogoBarragemComparator());
	}

	public void sorteiaJogos(ActionEvent e) {
		PersistenceHelper.initialize("ranking", rodadaEmFoco.getCiclo());
		List<CicloJogador> jogadores = new ArrayList<CicloJogador>(rodadaEmFoco.getCiclo().getRanking());
		CicloJogador.removeJogadoresQuePossuemJogos(jogadores, rodadaEmFoco.getJogadoresDosJogos());
		while (jogadores.size() >= 2) {
			int posicaoJogadorSorteado = rodadaEmFoco.getCiclo().sorteiaBaseadoNoRaio(jogadores.size());
			rodadaEmFoco.getJogos().add(
					rodadaEmFoco.criaJogoBarragem(jogadores.get(0).getJogador(), jogadores.get(posicaoJogadorSorteado)
							.getJogador()));

			jogadores.remove(posicaoJogadorSorteado);
			jogadores.remove(0);
		}
	}

	public void recalculaRankingEFechaRodada(ActionEvent e) {
		PersistenceHelper.initialize("rodadas", rodadaEmFoco.getCiclo());
		PersistenceHelper.initialize("ranking", rodadaEmFoco.getCiclo());
		List proxys = new ArrayList();
		for (Rodada rodada : rodadaEmFoco.getCiclo().getRodadas()) {
			proxys.add(rodada);
		}
		PersistenceHelper.initialize("jogos", proxys.toArray());

		rodadaEmFoco.recalculaPontuacoes();
		rodadaEmFoco.getCiclo().recalculaRanking();
		rodadaEmFoco.setFechada(Boolean.TRUE);

		rodadaEmFoco.getCiclo().getRodadas().remove(rodadaEmFoco);
		rodadaEmFoco.getCiclo().getRodadas().add(rodadaEmFoco);
		PersistenceHelper.persiste(rodadaEmFoco.getCiclo());
	}

	public void criaNovaRodada(ActionEvent e) {
		PersistenceHelper.initialize("ciclos", rodadaEmFoco.getCiclo().getBarragem());
		PersistenceHelper.initialize("rodadas", rodadaEmFoco.getCiclo());

		rodadaEmFoco.getCiclo().adicionaRodada();
		PersistenceHelper.persiste(rodadaEmFoco.getCiclo());

		setRequestAttribute("index", String.valueOf(rodadaEmFoco.getCiclo().getBarragem().getIndiceNaLista(
				rodadaEmFoco.getCiclo())));
		GerirCicloBean gerirCicloBean = (GerirCicloBean) getRequestAttribute("gerirCicloBean");
		gerirCicloBean.editaCiclo(null);
	}

	public void carregaRodada(Ciclo ciclo, int index) {
		rodadaEmFoco = (Rodada) PersistenceHelper
				.findByPk(Rodada.class, ciclo.getRodadas().get(index).getId(), "jogos");
		PersistenceHelper.initialize("ranking", rodadaEmFoco.getCiclo());
		List proxys = new ArrayList();
		for (Jogo jogo : rodadaEmFoco.getJogos()) {
			proxys.add(jogo.getPlacar());
			Collections.sort(jogo.getJogadoresEventos(), new JogadorEventoComparatorVencedorPrimeiro());
		}
		Collections.sort(rodadaEmFoco.getJogos(), new JogoBarragemComparator());
		PersistenceHelper.initialize("parciais", proxys.toArray());
	}
}

class JogadorEventoComparatorVencedorPrimeiro implements Comparator<JogadorEvento> {
	public int compare(JogadorEvento o1, JogadorEvento o2) {
		if (Boolean.TRUE.equals(((JogadorJogo) o1).getVencedor())) {
			return -1;
		} else if (Boolean.TRUE.equals(((JogadorJogo) o2).getVencedor())) {
			return 1;
		} else if (o1.getJogador() != null && o2.getJogador() != null) {
			return o1.getJogador().getNome().compareTo(o2.getJogador().getNome());
		}
		return -1;
	}
}

final class JogoBarragemComparator implements Comparator<JogoBarragem> {
	public int compare(JogoBarragem o1, JogoBarragem o2) {
		if (o1.getId() != null && o2.getId() != null) {
			return o1.getId().compareTo(o2.getId());
		} else if (o1.getData() != null && o2.getData() != null) {
			return o2.getData().compareTo(o1.getData());
		}
		return -1;
	}
}