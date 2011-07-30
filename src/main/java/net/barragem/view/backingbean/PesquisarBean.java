package net.barragem.view.backingbean;

import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.JogadorBo;
import net.barragem.business.bo.UsuarioBo;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;
import net.barragem.scaffold.JogadoresMaisRelevantesPrimeiroComparator;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class PesquisarBean extends BaseBean {

	private List<Jogador> jogadores;
	private List<Barragem> barragens;
	private List<Object> geral;
	private String pesquisa;
	private Paginavel<Jogador> paginacaoJogador;
	private Paginavel<Barragem> paginacaoBarragem;

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public List<Barragem> getBarragens() {
		return barragens;
	}

	public void setBarragens(List<Barragem> barragens) {
		this.barragens = barragens;
	}

	public List<Object> getGeral() {
		return geral;
	}

	public void setGeral(List<Object> geral) {
		this.geral = geral;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public String pesquisaJogador() {
		return pesquisaJogador(pesquisa);
	}

	public Paginavel<Jogador> getPaginacaoJogador() {
		return paginacaoJogador;
	}

	public void setPaginacaoJogador(Paginavel<Jogador> paginacao) {
		this.paginacaoJogador = paginacao;
	}

	public Paginavel<Barragem> getPaginacaoBarragem() {
		return paginacaoBarragem;
	}

	public void setPaginacaoBarragem(Paginavel<Barragem> paginacaoBarragem) {
		this.paginacaoBarragem = paginacaoBarragem;
	}

	public String pesquisaJogador(String pesquisa) {
		if (pesquisa != null && pesquisa.length() > 0) {
			List<Jogador> result = PersistenceHelper.findByNamedQuery("pesquisaJogadorQuery", new StringBuilder()
					.append("%").append(pesquisa).append("%").toString().toUpperCase());
			if (result.size() > 0) {
				getBo(UsuarioBo.class).carregaFotosJogadores(result);
				getBo(JogadorBo.class).marcaJogadoresNaoAdicionadosAUsuarioLogado(result);
				Collections.sort(result, new JogadoresMaisRelevantesPrimeiroComparator());
				paginacaoJogador = new PaginavelSampleImpl<Jogador>(result);
				jogadores = paginacaoJogador.getPagina();
				return "sucessoPesquisa";
			}

			messages.addInfoMessage("label_nenhum_resultado_encontrado", "label_nenhum_resultado_encontrado");
		} else {
			messages.addErrorMessage(null, "label_insira_um_texto_para_pesquisa");
		}
		return "";
	}

	public String pesquisaBarragem(String pesquisa) {
		if (pesquisa != null && pesquisa.length() > 0) {
			List<Barragem> result = PersistenceHelper.findByNamedQuery("pesquisaBarragemQuery", new StringBuilder()
					.append("%").append(pesquisa).append("%").toString().toUpperCase());
			if (result.size() > 0) {
				paginacaoBarragem = new PaginavelSampleImpl<Barragem>(result);
				barragens = paginacaoBarragem.getPagina();
				return "sucessoPesquisa";
			}

			messages.addInfoMessage("label_nenhum_resultado_encontrado", "label_nenhum_resultado_encontrado");
		} else {
			messages.addErrorMessage(null, "label_insira_um_texto_para_pesquisa");
		}
		return "";
	}

	public void pesquisaGeral(ActionEvent e) {
		// TODO
	}

	public void adicionaJogador(ActionEvent e) {
		getBo(JogadorBo.class).adicionaUsuario(paginacaoJogador.getPagina().get(getIndex()).getUsuarioCorrespondente());
		getBo(JogadorBo.class).marcaJogadoresNaoAdicionadosAUsuarioLogado(paginacaoJogador.getSourceList());
		addMensagemAtualizacaoComSucesso();
	}
}