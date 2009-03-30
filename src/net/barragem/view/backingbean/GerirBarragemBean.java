package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;
import net.barragem.util.PersistenceHelper;

public class GerirBarragemBean extends BaseBean {
	private Barragem barragemEmFoco;
	private List<Barragem> barragens;

	public GerirBarragemBean() {
		barragens = PersistenceHelper.findByNamedQuery("barragemPorUsuarioQuery", getUsuarioLogado());
	}

	public List<Barragem> getBarragens() {
		return barragens;
	}

	public void removeBarragem(ActionEvent e) {
		Barragem barragemARemover = barragens.get(Integer.parseInt(getServletRequest().getParameter("index")));
		PersistenceHelper.remove(barragemARemover);
		barragens.remove(barragemARemover);
	}

	public Barragem getBarragemEmFoco() {
		return barragemEmFoco;
	}

	public void setBarragemEmFoco(Barragem barragemEmFoco) {
		this.barragemEmFoco = barragemEmFoco;
	}

	public void novaBarragem(ActionEvent e) {
		barragemEmFoco = new Barragem();
		barragemEmFoco.setAdministrador(getUsuarioLogado());
		barragemEmFoco.setJogadores(new ArrayList<Jogador>());
	}

	public void adicionaJogador(ActionEvent e) {
		if (barragemEmFoco.getJogadores().size() < getMaxJogadoresDoAdministrador()) {
			Jogador jogador = new Jogador();
			jogador.setUsuarioDono(getUsuarioLogado());
			barragemEmFoco.getJogadores().add(new Jogador());
		}
	}

	public void removeJogador(ActionEvent e) {
		barragemEmFoco.getJogadores().remove(Integer.parseInt(getServletRequest().getParameter("index")));
	}

	public void salvaBarragem(ActionEvent e) {
		PersistenceHelper.persiste(barragemEmFoco);
		if (!barragens.contains(barragemEmFoco)) {
			barragens.add(barragemEmFoco);
		}
	}

	private Long getMaxJogadoresDoAdministrador() {
		return (Long) PersistenceHelper.findByNamedQuery("totalJogadoresDeUsuarioQuery", getUsuarioLogado()).get(0);
	}
}