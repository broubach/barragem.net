package net.barragem.view.backingbean;

import java.util.TreeSet;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.util.PersistenceHelper;

public class GerirJogadoresBean extends BaseBean {
	private Usuario usuario;

	public GerirJogadoresBean() {
		usuario = (Usuario) getSessionAttribute("usuario");
		if (usuario.getJogadores() == null) {
			usuario.setJogadores(new TreeSet<Jogador>());
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void adicionaJogador(ActionEvent e) {
		Jogador jogador = new Jogador();
		jogador.setUsuarioDono(usuario);
		usuario.getJogadores().add(jogador);
	}

	public void removeJogador(ActionEvent e) {
		usuario.getJogadores().remove(getServletRequest().getParameter("item"));
	}

	public void salva(ActionEvent e) {
		PersistenceHelper.persiste(usuario);
	}
}