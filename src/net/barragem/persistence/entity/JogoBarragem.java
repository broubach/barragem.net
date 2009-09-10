package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "jogobarragem")
@PrimaryKeyJoinColumn(name = "id")
public class JogoBarragem extends Jogo implements Cloneable {

	@ManyToOne
	private Rodada rodada;

	public Rodada getRodada() {
		return rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
	}

	public JogadorJogoBarragem getJogadorEvento(Jogador jogador) {
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (jogadorEvento.getJogador().equals(jogador)) {
				return (JogadorJogoBarragem) jogadorEvento;
			}
		}
		return null;
	}

	public Object clone() {
		JogoBarragem cloned = new JogoBarragem();
		cloned.setId(getId());
		cloned.setRodada(getRodada());
		cloned.setPlacar((Placar) getPlacar().clone());
		cloned.setTipo(getTipo());
		cloned.setData(getData());

		List<JogadorEvento> clonedJogadoresEventos = new ArrayList<JogadorEvento>();
		JogadorEvento clonedJogadorEvento = null;
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			clonedJogadorEvento = (JogadorEvento) ((JogadorJogoBarragem) jogadorEvento).clone();
			clonedJogadorEvento.setEvento(cloned);
			clonedJogadoresEventos.add(clonedJogadorEvento);
		}
		cloned.setJogadoresEventos(clonedJogadoresEventos);

		return cloned;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JogoBarragem other = (JogoBarragem) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}