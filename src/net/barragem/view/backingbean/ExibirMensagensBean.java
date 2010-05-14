package net.barragem.view.backingbean;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.MensagemBo;
import net.barragem.persistence.entity.Mensagem;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirMensagensBean extends BaseBean {

	private Usuario usuarioEmFoco;
	private String mensagem;
	private Paginavel<Mensagem> paginacaoMensagens;
	private Integer totalMensagens;

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Paginavel<Mensagem> getPaginacaoMensagens() {
		return paginacaoMensagens;
	}

	public void setPaginacaoMensagens(Paginavel<Mensagem> paginacaoMensagens) {
		this.paginacaoMensagens = paginacaoMensagens;
	}

	public Integer getTotalMensagens() {
		return totalMensagens;
	}

	public void setTotalMensagens(Integer totalMensagens) {
		this.totalMensagens = totalMensagens;
	}

	public String envia() {
		getBo(MensagemBo.class).envia(usuarioEmFoco, mensagem);
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery("mensagemQuery",
				usuarioEmFoco), 5);
		totalMensagens = paginacaoMensagens.getSourceList().size();
		mensagem = "";
		addMensagemAtualizacaoComSucesso();
		return "";
	}

	public void exclui(ActionEvent e) {
		Mensagem focus = paginacaoMensagens.getPosteriorImediatoOuAnteriorImediato(getIndex());
		PersistenceHelper.remove(paginacaoMensagens.getPagina().get(getIndex()));
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery("mensagemQuery",
				usuarioEmFoco), focus, 5);
		totalMensagens = paginacaoMensagens.getSourceList().size();
		addMensagemAtualizacaoComSucesso();
	}

	public void exibeMensagens(ActionEvent e) {
		usuarioEmFoco = PersistenceHelper.findByPk(Usuario.class, getId());
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery("mensagemQuery",
				usuarioEmFoco), 5);
		totalMensagens = paginacaoMensagens.getSourceList().size();
	}
}