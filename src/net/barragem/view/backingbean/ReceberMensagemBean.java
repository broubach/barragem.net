package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.MensagemBo;
import net.barragem.persistence.entity.Mensagem;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ReceberMensagemBean extends BaseBean {

	private List<Mensagem> mensagens;
	private List<String> respostas;

	public ReceberMensagemBean() {
		mensagens = PersistenceHelper.findByNamedQuery("mensagemQuery", getUsuarioLogado());
		respostas = new ArrayList<String>();
		for (int i = 0; i < mensagens.size(); i++) {
			respostas.add("");
		}
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

	public List<String> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<String> respostas) {
		this.respostas = respostas;
	}

	public String responde() {
		getBo(MensagemBo.class).responde(mensagens.get(getIndex()), respostas.get(getIndex()));
		addMensagemAtualizacaoComSucesso();
		return "";
	}

	public void exclui(ActionEvent e) {
		PersistenceHelper.remove(mensagens.get(getIndex()));
		addMensagemAtualizacaoComSucesso();
		mensagens = PersistenceHelper.findByNamedQuery("mensagemQuery", getUsuarioLogado());
	}
}