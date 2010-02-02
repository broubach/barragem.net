package net.barragem.view.backingbean;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.util.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GerirJogadorBean extends BaseBean {
	private Usuario usuarioEmFoco;
	private String usuarioNome;

	public GerirJogadorBean() {
		usuarioEmFoco = getUsuarioLogado();
		PersistenceHelper.initialize("jogadores", usuarioEmFoco);
	}

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public String getUsuarioNome() {
		return usuarioNome;
	}

	public void setUsuarioNome(String usuarioNome) {
		this.usuarioNome = usuarioNome;
	}

	public void adicionaJogador(ActionEvent e) {
		if (usuarioNome != null && usuarioNome.length() > 0) {
			Jogador jogador = new Jogador();
			jogador.setNome(usuarioNome);
			jogador.setUsuarioDono(usuarioEmFoco);
			usuarioEmFoco.getJogadores().add(jogador);

			PersistenceHelper.persiste(usuarioEmFoco);
		} else {
			messages.addErrorMessage(null, "label_digite_o_nome_do_novo_jogador");
		}
	}

	public void removeJogador(ActionEvent e) {
		PersistenceHelper.remove(usuarioEmFoco.getJogadores().remove(getIndex()));
	}
}