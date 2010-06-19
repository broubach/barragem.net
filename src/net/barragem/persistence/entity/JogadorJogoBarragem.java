package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "jogadorjogobarragem")
public class JogadorJogoBarragem extends JogadorJogo {

	private Integer pontuacaoObtida;

	public Integer getPontuacaoObtida() {
		return pontuacaoObtida;
	}

	public void setPontuacaoObtida(Integer pontuacaoObtida) {
		this.pontuacaoObtida = pontuacaoObtida;
	}

	public void cloneTo(Object newObject) {
		super.cloneTo(newObject);
		((JogadorJogoBarragem) newObject).setPontuacaoObtida(pontuacaoObtida);
	}
}
