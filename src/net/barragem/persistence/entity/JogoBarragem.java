package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import net.barragem.util.ValidatableSampleImpl;

@Entity
@Table(name = "jogobarragem")
@PrimaryKeyJoinColumn(name = "id")
public class JogoBarragem extends Jogo implements Cloneable, Validatable {

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

	public Jogador obtemVencedor(Jogador jogadorVencedorWo) {
		if (getPlacar().getWo() && jogadorVencedorWo == null) {
			return null;
		} else if (getPlacar().getWo() && jogadorVencedorWo != null) {
			return jogadorVencedorWo;
		}
		Map<Jogador, Integer> totalSetsVencidos = new HashMap<Jogador, Integer>();
		Jogador teoricoVencedor = getJogadoresEventos().get(0).getJogador();
		Jogador teoricoPerdedor = getJogadoresEventos().get(1).getJogador();
		totalSetsVencidos.put(teoricoVencedor, new Integer(0));
		totalSetsVencidos.put(teoricoPerdedor, new Integer(0));
		Parcial parcial = null;
		for (int i = 0; i < getPlacar().getParciais().size(); i++) {
			parcial = getPlacar().getParciais().get(i);
			if (parcial.getParcialVencedor() == null || parcial.getParcialPerdedor() == null) {
				continue;
			}
			if (parcial.getParcialVencedor() > parcial.getParcialPerdedor()) {
				totalSetsVencidos.put(teoricoVencedor, totalSetsVencidos.get(teoricoVencedor) + 1);
			} else if (parcial.getParcialPerdedor() > parcial.getParcialVencedor()) {
				totalSetsVencidos.put(teoricoPerdedor, totalSetsVencidos.get(teoricoPerdedor) + 1);
			}
		}
		if (totalSetsVencidos.get(teoricoVencedor) > totalSetsVencidos.get(teoricoPerdedor)) {
			return teoricoVencedor;
		}
		if (totalSetsVencidos.get(teoricoPerdedor) > totalSetsVencidos.get(teoricoVencedor)) {
			return teoricoPerdedor;
		}

		return null;
	}

	public void marcaVencedor(Jogador vencedor) {
		JogadorJogoBarragem jogadorJogoBarragem = null;
		for (int i = 0; i < getJogadoresEventos().size(); i++) {
			jogadorJogoBarragem = (JogadorJogoBarragem) getJogadoresEventos().get(i);
			if (jogadorJogoBarragem.getJogador() == vencedor) {
				jogadorJogoBarragem.setVencedor(true);
			} else {
				jogadorJogoBarragem.setVencedor(false);
			}
		}
	}

	public void desmarcaVencedor() {
		JogadorJogoBarragem jogadorJogoBarragem = null;
		for (int i = 0; i < getJogadoresEventos().size(); i++) {
			jogadorJogoBarragem = (JogadorJogoBarragem) getJogadoresEventos().get(i);
			jogadorJogoBarragem.setVencedor(false);
		}
	}

	public List<String> validate() {
		return new ValidatableSampleImpl(this).validate();
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
