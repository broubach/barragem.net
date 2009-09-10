package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.util.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GerirJogadorBean extends BaseBean {
	private Usuario usuario;
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private List<Jogador> jogadoresRemovidos = new ArrayList<Jogador>();

	public GerirJogadorBean() {
		usuario = (Usuario) getSessionAttribute("usuario");
		PersistenceHelper.initialize("jogadores", usuario);
		jogadores.addAll(usuario.getJogadores());
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public void adicionaJogador(ActionEvent e) {
		Jogador jogador = new Jogador();
		jogador.setUsuarioDono(usuario);
		jogadores.add(jogador);
	}

	public void removeJogador(ActionEvent e) {
		jogadoresRemovidos.add(jogadores.remove(getIndex()));
	}

	public void salva(ActionEvent e) {
		for (Jogador jogadorRemovido : jogadoresRemovidos) {
			PersistenceHelper.remove(jogadorRemovido);
		}
		usuario.setJogadores(jogadores);
		PersistenceHelper.persiste(usuario);

		jogadores = new ArrayList<Jogador>();
		jogadores.addAll(usuario.getJogadores());
		setUsuarioLogado(usuario);
	}
}