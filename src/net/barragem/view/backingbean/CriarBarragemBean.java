package net.barragem.view.backingbean;

import java.util.TreeSet;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;

public class CriarBarragemBean extends BaseBean {
	private Barragem barragem;

	public CriarBarragemBean() {
		barragem = new Barragem();
		barragem.setJogadores(new TreeSet<Jogador>());
	}

	public void adicionaJogador(ActionEvent e) {
		barragem.getJogadores().add(new Jogador());
	}

	public void removeJogador(ActionEvent e) {
		barragem.getJogadores().remove(Integer.parseInt(getServletRequest().getParameter("index")));
	}

	public Barragem getBarragem() {
		return barragem;
	}

	public void setBarragem(Barragem barragem) {
		this.barragem = barragem;
	}
}
