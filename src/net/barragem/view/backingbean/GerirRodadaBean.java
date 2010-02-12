package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.Placar;
import net.barragem.persistence.entity.Rodada;
import net.barragem.util.PersistenceHelper;
import net.barragem.view.backingbean.componentes.JogadorEventoComparatorVencedorPrimeiro;
import net.barragem.view.backingbean.componentes.JogoBarragemComparator;

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

		setSessionAttribute("gerirJogoBarragemBean", jogoBarragemBean);
	}

	public void adicionaJogo(ActionEvent e) {
		GerirJogoBarragemBean jogoBarragemBean = new GerirJogoBarragemBean();
		jogoBarragemBean.adicionaJogo(rodadaEmFoco);

		setSessionAttribute("gerirJogoBarragemBean", jogoBarragemBean);
	}

	public void removeJogo(ActionEvent e) {
		PersistenceHelper.remove(rodadaEmFoco.getJogos().remove(getIndex()));
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

		salvaRodada(e);
	}

	private void salvaRodada(ActionEvent e) {
		PersistenceHelper.persiste(rodadaEmFoco);
		Collections.sort(rodadaEmFoco.getJogos(), new JogoBarragemComparator());
	}

	public void recalculaRankingEFechaRodada(ActionEvent e) {
		PersistenceHelper.initialize("rodadas", rodadaEmFoco.getCiclo());
		PersistenceHelper.initialize("ranking", rodadaEmFoco.getCiclo());
		List<Rodada> proxys = new ArrayList<Rodada>();
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

		GerirCicloBean cicloBean = (GerirCicloBean) getRequestAttribute("gerirCicloBean");
		cicloBean.editaCiclo(e);
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
		List<Placar> proxys = new ArrayList<Placar>();
		for (Jogo jogo : rodadaEmFoco.getJogos()) {
			proxys.add(jogo.getPlacar());
			Collections.sort(jogo.getJogadoresEventos(), new JogadorEventoComparatorVencedorPrimeiro());
		}
		Collections.sort(rodadaEmFoco.getJogos(), new JogoBarragemComparator());
		PersistenceHelper.initialize("parciais", proxys.toArray());
	}

	public boolean isRodadaAnteriorEmAberto() {
		if (rodadaEmFoco.getNumero().equals(new Integer(1))) {
			return false;
		}

		PersistenceHelper.initialize("rodadas", rodadaEmFoco.getCiclo());
		return !rodadaEmFoco.getCiclo().getRodadas().get(rodadaEmFoco.getNumero() - 2).getFechada();
	}
}