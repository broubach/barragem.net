package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.JogadorBo;
import net.barragem.business.bo.UsuarioBo;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirPerfilBean extends BaseBean {

	private Usuario usuarioEmFoco;
	private Paginavel<Jogador> paginacaoJogador;

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public Paginavel<Jogador> getPaginacaoJogador() {
		return paginacaoJogador;
	}

	public void setPaginacaoJogador(Paginavel<Jogador> paginacaoJogador) {
		this.paginacaoJogador = paginacaoJogador;
	}

	public boolean getUsuarioJahAdicionado() {
		return getUsuarioLogado().hasJogador(usuarioEmFoco);
	}

	public void adicionaUsuario(ActionEvent e) {
		if (!getUsuarioLogado().jahPossuiJogador(usuarioEmFoco.getNomeCompletoCapital())) {
			getBo(JogadorBo.class).adicionaUsuario(usuarioEmFoco);
			addMensagemAtualizacaoComSucesso();
		} else {
			messages.addErrorMessage(null, "label_jogador_com_mesmo_nome_jah_existente");
		}
	}

	public void exibePerfil(ActionEvent e) {
		List<Usuario> usuarios = PersistenceHelper.findByNamedQuery("perfilQuery", getId());
		exibePerfil(usuarios.get(0));
	}

	public void exibePerfil(Usuario usuario) {
		usuarioEmFoco = usuario;
		PersistenceHelper.initialize("jogadores", usuarioEmFoco);
		PersistenceHelper.initialize("barragens", usuarioEmFoco);
		getBo(UsuarioBo.class).carregaFotosJogadores(usuarioEmFoco.getJogadores());
		paginacaoJogador = new PaginavelSampleImpl<Jogador>(usuarioEmFoco.getJogadoresSemOUsuarioCorrespondente(),
				null, 6);
	}

	public List<Barragem> getBarragens() {
		return getBarragens(usuarioEmFoco.getId());
	}
}