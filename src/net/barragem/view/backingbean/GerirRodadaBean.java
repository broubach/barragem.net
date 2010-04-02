package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.barragem.business.bo.RodadaBo;
import net.barragem.exception.SaldoInsuficienteException;
import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Placar;
import net.barragem.persistence.entity.Rodada;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.JogadorEventoComparatorVencedorPrimeiro;
import net.barragem.view.backingbean.componentes.JogoBarragemComparator;
import net.barragem.view.backingbean.componentes.RodadaJogosBarragemMestreDetalhe;

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
		if (getContaUsuario().possuiSaldoSuficiente(1)) {
			GerirJogoBarragemBean jogoBarragemBean = new GerirJogoBarragemBean();
			jogoBarragemBean.adicionaJogo(rodadaEmFoco);

			setRequestAttribute("gerirJogoBarragemBean", jogoBarragemBean);
		} else {
			messages.addErrorMessage("saldo_insuficiente_exception", "label_saldo_insuficiente");
		}
	}

	public void removeJogo(ActionEvent e) {
		PersistenceHelper.remove(rodadaEmFoco.getJogos().remove(getIndex()));
		PersistenceHelper.persiste(getContaUsuario().criaOperacaoDevolucao(1));
		PersistenceHelper.persiste(getContaUsuario());
	}

	public void sorteiaJogos(ActionEvent e) {
		try {
			getBo(RodadaBo.class).sorteiaJogos(rodadaEmFoco);
		} catch (SaldoInsuficienteException e1) {
			messages.addErrorMessage("saldo_insuficiente_exception", "label_saldo_insuficiente");
		}
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
		for (JogoBarragem jogoBarragem : rodadaEmFoco.getJogos()) {
			RodadaJogosBarragemMestreDetalhe.removeSetsIncompletos(jogoBarragem.getPlacar());
		}
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

	public List<SelectItem> getListaJogadoresHabilitados() {
		List<Jogador> jogadores = rodadaEmFoco.getCiclo().getJogadoresHabilitadosDoRanking();

		List<SelectItem> items = new ArrayList<SelectItem>();
		Jogador jogador = null;
		for (Iterator<Jogador> it = jogadores.iterator(); it.hasNext();) {
			jogador = it.next();
			SelectItem selectItem = new SelectItem(jogador, jogador.getNome());
			items.add(selectItem);
		}
		return items;
	}

	public boolean getPossuiSaldoParaUmJogo() {
		if (!getContaUsuario().possuiSaldoSuficiente(1)) {
			messages.addErrorMessage("saldo_insuficiente_exception", "label_saldo_insuficiente");
			return false;
		}
		return true;
	}
}