package net.barragem.persistence.entity;

import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class JogoBarragem extends Jogo {
	@ManyToOne
	private Barragem barragem;

	public Barragem getBarragem() {
		return barragem;
	}

	public void setBarragem(Barragem barragem) {
		this.barragem = barragem;
	}

	public void inicializaParaDuplas() {
		jogadoresEventos = new HashSet<JogadorEvento>();
		jogadoresEventos.add(new JogadorEvento());
		jogadoresEventos.add(new JogadorEvento());
		jogadoresEventos.add(new JogadorEvento());
		jogadoresEventos.add(new JogadorEvento());
	}
}