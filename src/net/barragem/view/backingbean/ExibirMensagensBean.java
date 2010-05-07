package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.MensagemBo;
import net.barragem.persistence.entity.Mensagem;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirMensagensBean extends BaseBean {

	private Usuario usuarioEmFoco;
	private List<Mensagem> mensagens;
	private String mensagem;

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String envia() {
		getBo(MensagemBo.class).envia(usuarioEmFoco, mensagem);
		addMensagemAtualizacaoComSucesso();
		return "";
	}

	public void exclui(ActionEvent e) {
		PersistenceHelper.remove(mensagens.get(getIndex()));
		addMensagemAtualizacaoComSucesso();
		mensagens = PersistenceHelper.findByNamedQuery("mensagemQuery", getUsuarioLogado());
	}

	public void exibeMensagens(ActionEvent e) {
		usuarioEmFoco = PersistenceHelper.findByPk(Usuario.class, getId());
		mensagens = PersistenceHelper.findByNamedQuery("mensagemQuery", usuarioEmFoco);
	}
}