package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.MensagemBo;
import net.barragem.persistence.entity.Mensagem;
import net.barragem.scaffold.PaginacaoListener;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.scaffold.ReceberMensagemPaginacaoListener;
import net.barragem.view.backingbean.componentes.TextoEFlagPrivadoDto;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ReceberMensagemBean extends BaseBean {

	private List<TextoEFlagPrivadoDto> respostas;
	private Paginavel<Mensagem> paginacaoMensagens;

	public ReceberMensagemBean() {
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery(
				"todasMensagensQuery", getUsuarioLogado()), null, 5);
		PaginacaoListener<Mensagem> paginacaoListener = new ReceberMensagemPaginacaoListener(this);
		paginacaoMensagens.setListener(paginacaoListener);
		paginacaoListener.afterPageChange(paginacaoMensagens);

		PersistenceHelper.execute("marcaMensagensComoLidasQuery", getUsuarioLogado().getId());
	}

	public List<TextoEFlagPrivadoDto> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<TextoEFlagPrivadoDto> respostas) {
		this.respostas = respostas;
	}

	public Paginavel<Mensagem> getPaginacaoMensagens() {
		return paginacaoMensagens;
	}

	public void setPaginacaoMensagens(Paginavel<Mensagem> paginacaoMensagens) {
		this.paginacaoMensagens = paginacaoMensagens;
	}

	public String responde() {
		getBo(MensagemBo.class).responde(paginacaoMensagens.getPagina().get(getIndex()),
				respostas.get(getIndex()).getTexto(), respostas.get(getIndex()).getPrivada());
		addMensagemAtualizacaoComSucesso();
		return "";
	}

	public void exclui(ActionEvent e) {
		PersistenceHelper.remove(paginacaoMensagens.getPagina().get(getIndex()));
		addMensagemAtualizacaoComSucesso();
		paginacaoMensagens = new PaginavelSampleImpl<Mensagem>(PersistenceHelper.findByNamedQuery(
				"todasMensagensQuery", getUsuarioLogado()), paginacaoMensagens.getPaginaAtual(), 5);

		PaginacaoListener<Mensagem> paginacaoListener = new ReceberMensagemPaginacaoListener(this);
		paginacaoMensagens.setListener(paginacaoListener);
		paginacaoListener.afterPageChange(paginacaoMensagens);
	}
}