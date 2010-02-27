package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

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
		getUsuarioLogado().getJogadores().add(novoJogador);
		PersistenceHelper.persiste(getUsuarioLogado());
		addMensagemAtualizacaoComSucesso();
		// TODO: redigir conteudo de email
	}

	public void exibePerfil(ActionEvent e) {
		List<Usuario> usuarios = PersistenceHelper.findByNamedQuery("perfilQuery", getId());
		usuarioEmFoco = usuarios.get(0);
		PersistenceHelper.initialize("jogadores", usuarioEmFoco);
		PersistenceHelper.initialize("barragens", usuarioEmFoco);
	}

	public void exibePainelBarragem(ActionEvent e) {
		Barragem barragem = (Barragem) PersistenceHelper.findByPk(Barragem.class, getId(), "ciclos");

		ExibirPainelBarragemBean painelBarragemBean = new ExibirPainelBarragemBean();
		painelBarragemBean.setCicloEmFoco(barragem.getCiclos().get(0));
		setSessionAttribute("exibirPainelBarragemBean", painelBarragemBean);
	}

	public List<Barragem> getBarragens() {
		return getBarragens(usuarioEmFoco.getId());
	}
}