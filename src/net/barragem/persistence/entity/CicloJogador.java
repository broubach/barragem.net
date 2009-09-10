package net.barragem.persistence.entity;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ciclojogador")
public class CicloJogador extends BaseEntity {

	@ManyToOne
	private Jogador jogador;

	@ManyToOne
	private Ciclo ciclo;

	private Integer ranking;
	private Integer pontuacao;

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Ciclo getCiclo() {
		return ciclo;
	}

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}

	public static void removeJogadoresQuePossuemJogos(List<CicloJogador> cicloJogadores,
			List<Jogador> jogadoresQuePossuemJogos) {
		for (Iterator<CicloJogador> it = cicloJogadores.iterator(); it.hasNext();) {
			CicloJogador cicloJogador = it.next();
			if (jogadoresQuePossuemJogos.contains(cicloJogador.getJogador())) {
				it.remove();
			}
		}
	}
}