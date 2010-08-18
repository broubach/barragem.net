package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.MensagemBo;
import net.barragem.persistence.entity.Mensagem;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ReceberMensagemBean extends BaseBean {

	private List<String> respostas;
	private Paginavel<Mensagem> paginacaoMensagens;
	private Integer totalMensagens;

	public ReceberMensagemBean() {
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery("mensagemQuery",
				getUsuarioLogado()), null, 6);
		respostas = new ArrayList<String>();
		for (int i = 0; i < paginacaoMensagens.getPagina().size(); i++) {
			respostas.add("");
		}
		totalMensagens = paginacaoMensagens.getSourceList().size();
	}

	public List<String> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<String> respostas) {
		this.respostas = respostas;
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

	public String responde() {
		getBo(MensagemBo.class).responde(paginacaoMensagens.getPagina().get(getIndex()), respostas.get(getIndex()));
		addMensagemAtualizacaoComSucesso();
		return "";
	}

	public void exclui(ActionEvent e) {
		PersistenceHelper.remove(paginacaoMensagens.getPagina().get(getIndex()));
		addMensagemAtualizacaoComSucesso();
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery("mensagemQuery",
				getUsuarioLogado()), paginacaoMensagens.getPaginaAtual(), 6);
		totalMensagens = paginacaoMensagens.getSourceList().size();
	}
}