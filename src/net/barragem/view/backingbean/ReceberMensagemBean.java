package net.barragem.view.backingbean;

import java.util.List;

import net.barragem.persistence.entity.Mensagem;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ReceberMensagemBean extends BaseBean {

	private List<Mensagem> mensagens;
	private String mensagemResposta;

	public ReceberMensagemBean() {
		mensagens = PersistenceHelper.findByNamedQuery("mensagemQuery", getUsuarioLogado());
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

	public String getMensagemResposta() {
		return mensagemResposta;
	}

	public void setMensagemResposta(String mensagemResposta) {
		this.mensagemResposta = mensagemResposta;
	}
}
