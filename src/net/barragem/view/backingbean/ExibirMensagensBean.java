package net.barragem.view.backingbean;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.MensagemBo;
import net.barragem.persistence.entity.Mensagem;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.TextoEFlagPrivadoDto;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirMensagensBean extends BaseBean {

	private Usuario usuarioEmFoco;
	private TextoEFlagPrivadoDto mensagem = new TextoEFlagPrivadoDto();
	private Paginavel<Mensagem> paginacaoMensagens;

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public TextoEFlagPrivadoDto getMensagem() {
		return mensagem;
	}

	public void setMensagem(TextoEFlagPrivadoDto mensagem) {
		this.mensagem = mensagem;
	}

	public Paginavel<Mensagem> getPaginacaoMensagens() {
		return paginacaoMensagens;
	}

	public void setPaginacaoMensagens(Paginavel<Mensagem> paginacaoMensagens) {
		this.paginacaoMensagens = paginacaoMensagens;
	}

	public String envia() {
		Mensagem focus = getBo(MensagemBo.class).envia(usuarioEmFoco, mensagem.getTexto(), mensagem.getPrivada());
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery("mensagensVisiveisQuery",
				getUsuarioLogado(), usuarioEmFoco), focus, null, 5);
		mensagem = new TextoEFlagPrivadoDto();
		addMensagemAtualizacaoComSucesso();
		return "";
	}

	public void exclui(ActionEvent e) {
		PersistenceHelper.remove(paginacaoMensagens.getPagina().get(getIndex()));
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery("mensagensVisiveisQuery",
				getUsuarioLogado(), usuarioEmFoco), paginacaoMensagens.getPaginaAtual(), 5);
		addMensagemAtualizacaoComSucesso();
	}

	public void exibeMensagens(ActionEvent e) {
		usuarioEmFoco = PersistenceHelper.findByPk(Usuario.class, getId());
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery("mensagensVisiveisQuery",
				getUsuarioLogado(), usuarioEmFoco), null, 5);
	}
	
	public Integer getTotalMensagens() {
		return paginacaoMensagens.getSourceList().size();
	}

	public Integer getTotalMensagensPrivadas() {
		Integer result = 0;
		for (Mensagem mensagem : paginacaoMensagens.getSourceList()) {
			if (mensagem.isPrivada()) {
				result++;
			}
		}
		return result;
	}
}