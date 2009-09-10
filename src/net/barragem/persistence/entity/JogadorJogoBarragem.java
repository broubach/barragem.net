package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "jogadorjogobarragem")
public class JogadorJogoBarragem extends JogadorJogo implements Cloneable {

	private Integer pontuacaoObtida;

	public Integer getPontuacaoObtida() {
		return pontuacaoObtida;
	}

	public void setPontuacaoObtida(Integer pontuacaoObtida) {
		this.pontuacaoObtida = pontuacaoObtida;
	}

	public Object clone() {
		JogadorJogoBarragem cloned = new JogadorJogoBarragem();
		cloned.setComentario(getComentario());
		cloned.setEvento(getEvento());
		cloned.setId(getId());
		cloned.setJogador(getJogador());
		cloned.setPontuacaoObtida(getPontuacaoObtida());
		cloned.setVencedor(getVencedor());
		return cloned;
	}
}
