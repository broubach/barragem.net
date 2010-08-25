package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.Rodada;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.EventoMaisRecenteComparator;
import net.barragem.view.backingbean.componentes.JogadorEventoComparatorVencedorPrimeiro;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirPainelBarragemBean extends BaseBean {

	private Ciclo cicloEmFoco;
	private Integer startIndex = new Integer(0);
	private Integer endIndex = new Integer(1);
	private Integer numeroRodada;

	private List<Rodada> rodadas;
	private List<CicloJogador> ranking;

	public ExibirPainelBarragemBean() {
		Barragem barragem = (Barragem) PersistenceHelper.findByPk(Barragem.class, getId(), "ciclos");
		cicloEmFoco = barragem.getCiclos().get(0);

		PersistenceHelper.initialize("rodadas", cicloEmFoco);
		PersistenceHelper.initialize("ranking", cicloEmFoco);
		rodadas = new ArrayList<Rodada>(cicloEmFoco.getRodadas());
		Collections.reverse(rodadas);
		inicializaRodadas();

		ranking = cicloEmFoco.getRanking();
	}

	private void inicializaRodadas() {
		for (int i = startIndex; i <= endIndex; i++) {
			if (i < rodadas.size()) {
				PersistenceHelper.initialize("jogos", rodadas.get(i));
				for (Jogo jogoBarragem : rodadas.get(i).getJogos()) {
					PersistenceHelper.initialize("parciais", jogoBarragem.getPlacar());
					Collections.sort(jogoBarragem.getJogadoresEventos(), new JogadorEventoComparatorVencedorPrimeiro());
				}
				Collections.sort(rodadas.get(i).getJogos(), new EventoMaisRecenteComparator());
			}
		}
	}

	public Ciclo getCicloEmFoco() {
		return cicloEmFoco;
	}

	public void setCicloEmFoco(Ciclo cicloEmFoco) {
		this.cicloEmFoco = cicloEmFoco;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer beginIndex) {
		this.startIndex = beginIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public Integer getNumeroRodada() {
		return numeroRodada;
	}

	public void setNumeroRodada(Integer numeroRodada) {
		this.numeroRodada = numeroRodada;
	}

	public List<Rodada> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<Rodada> rodadas) {
		this.rodadas = rodadas;
	}

	public List<CicloJogador> getRanking() {
		return ranking;
	}

	public void setRanking(List<CicloJogador> ranking) {
		this.ranking = ranking;
	}

	public void visualizaCiclo(ActionEvent e) {
		if (getIndex() != -1) {
			Ciclo ciclo = cicloEmFoco.getBarragem().getCiclos().get(getIndex());
			cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, ciclo.getId(), "ranking", "rodadas");
		} else {
			cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, cicloEmFoco.getId(), "ranking", "rodadas");
		}
		PersistenceHelper.initialize("ciclos", cicloEmFoco.getBarragem());
	}

	public void visualizaRodada(ActionEvent e) {
		if (numeroRodada != null && numeroRodada > 0 && numeroRodada <= cicloEmFoco.getRodadas().size()) {
			startIndex = cicloEmFoco.getRodadas().size() - numeroRodada;
			endIndex = startIndex + 1;
		} else {
			numeroRodada = cicloEmFoco.getRodadas().size();
			startIndex = 0;
			endIndex = 1;
		}
		inicializaRodadas();
		refreshView();
	}
}