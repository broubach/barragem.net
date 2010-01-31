package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.util.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirPerfilBean extends BaseBean {

	private Usuario usuarioEmFoco;

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public boolean getUsuarioJahAdicionado() {
		return getUsuarioLogado().hasJogador(usuarioEmFoco);
	}

	public void adicionaUsuario(ActionEvent e) {
		Jogador novoJogador = new Jogador();
		novoJogador.setNome(usuarioEmFoco.getNomeCompletoCapital());
		novoJogador.setUsuarioCorrespondente(usuarioEmFoco);
		novoJogador.setUsuarioDono(getUsuarioLogado());
		PersistenceHelper.persiste(novoJogador);
		addMensagemAtualizacaoComSucesso();
	}

	public void exibePerfil(ActionEvent e) {
		List<Usuario> usuarios = PersistenceHelper.findByNamedQuery("perfilQuery", getId());
		PersistenceHelper.initialize("jogadores", getUsuarioLogado());

		usuarioEmFoco = usuarios.get(0);
	}
}
