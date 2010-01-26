package net.barragem.view.backingbean;

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

	public List<Rodada> getRodadasFetch() {
		PersistenceHelper.initialize("rodadas", cicloEmFoco);
		for (int i = startIndex; i < endIndex; i++) {
			PersistenceHelper.initialize("jogos", cicloEmFoco.getRodadas().get(i));
			for (JogoBarragem jogoBarragem : cicloEmFoco.getRodadas().get(i).getJogos()) {
				PersistenceHelper.initialize("parciais", jogoBarragem.getPlacar());
			}
		}
		return cicloEmFoco.getRodadas();
	}

	public List<CicloJogador> getRankingFetch() {
		PersistenceHelper.initialize("ranking", cicloEmFoco);
		for (CicloJogador cicloJogador : cicloEmFoco.getRanking()) {
			if (cicloJogador.getJogador().getUsuarioCorrespondente() != null
					&& cicloJogador.getJogador().getUsuarioCorrespondente().getPerfil() != null) {
				PersistenceHelper.initialize("foto", cicloJogador.getJogador().getUsuarioCorrespondente().getPerfil());
			}
		}
		return cicloEmFoco.getRanking();
	}
}