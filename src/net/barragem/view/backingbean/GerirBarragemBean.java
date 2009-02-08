package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.util.PersistenceHelper;

public class GerirBarragemBean extends BaseBean {
	private Usuario usuario;
	private Barragem barragem;

	public Usuario getUsuario() {
		if (usuario == null) {
			StringBuilder query = new StringBuilder();
			query.append("from Usuario usuario left outer join fetch usuario.barragens ");
			query.append("where usuario.login ='").append(getUsuarioLogado().getLogin()).append("' ");
			List<Usuario> usuarios = PersistenceHelper.find(query.toString());
			usuario = usuarios.get(0);
		}
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Barragem getBarragem() {
		return barragem;
	}

	public void setBarragem(Barragem barragem) {
		this.barragem = barragem;
	}

	public void adicionaJogador(ActionEvent e) {
		if (barragem.getJogadores().size() < getUsuarioLogado().getJogadores().size()) {
			Jogador jogador = new Jogador();
			jogador.setUsuarioDono(getUsuarioLogado());
			barragem.getJogadores().add(new Jogador());
		}
	}

	public void removeJogador(ActionEvent e) {
		barragem.getJogadores().remove(Integer.parseInt(getServletRequest().getParameter("index")));
	}

	public void novaBarragem(ActionEvent e) {
		barragem = new Barragem();
		barragem.setAdministrador(getUsuarioLogado());
		barragem.setJogadores(new ArrayList<Jogador>());
	}

	public void salva(ActionEvent e) {
		PersistenceHelper.persiste(barragem);
	}
}