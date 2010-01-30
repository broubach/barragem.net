package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Rodada;
import net.barragem.util.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirPainelBarragemBean extends BaseBean {

	private Ciclo cicloEmFoco;
	private Integer startIndex = new Integer(0);
	private Integer endIndex = new Integer(1);
	private Integer numeroRodada;

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

	public void visualizaCiclo(ActionEvent e) {
		if (getIndex() != -1) {
			Ciclo ciclo = cicloEmFoco.getBarragem().getCiclos().get(getIndex());
			cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, ciclo.getId(), "ranking", "rodadas");
		} else {
			cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, cicloEmFoco.getId(), "ranking", "rodadas");
		}
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
		refreshView();
	}

	public List<Rodada> getRodadasFetch() {
		PersistenceHelper.initialize("rodadas", cicloEmFoco);
		List<Rodada> result = new ArrayList<Rodada>(cicloEmFoco.getRodadas());
		Collections.reverse(result);
		for (int i = startIndex; i <= endIndex; i++) {
			if (i < result.size()) {
				PersistenceHelper.initialize("jogos", result.get(i));
				for (JogoBarragem jogoBarragem : result.get(i).getJogos()) {
					PersistenceHelper.initialize("parciais", jogoBarragem.getPlacar());
				}
			}
		}
		return result;
	}

	public List<CicloJogador> getRankingFetch() {
		PersistenceHelper.initialize("ranking", cicloEmFoco);
		return cicloEmFoco.getRanking();
	}
}