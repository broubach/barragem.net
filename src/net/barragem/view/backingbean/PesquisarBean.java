package net.barragem.view.backingbean;

import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;
import net.barragem.scaffold.JogadoresComCorrespondenciaPrimeiroComparator;
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
	private Paginavel<Jogador> paginacao;

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

	public Paginavel<Jogador> getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginavel<Jogador> paginacao) {
		this.paginacao = paginacao;
	}

	public String pesquisaJogador(String pesquisa) {
		if (pesquisa != null && pesquisa.length() > 0) {
			List<Jogador> result = PersistenceHelper.findByNamedQuery("pesquisaJogadorQuery", new StringBuilder()
					.append("%").append(pesquisa).append("%").toString().toUpperCase());
			if (result.size() > 0) {
				Collections.sort(result, new JogadoresComCorrespondenciaPrimeiroComparator());
				paginacao = new PaginavelSampleImpl<Jogador>(result);
				jogadores = paginacao.getPagina();
				return "sucessoPesquisa";
			}

			messages.addInfoMessage("label_nenhum_resultado_encontrado", "label_nenhum_resultado_encontrado");
		} else {
			messages.addErrorMessage(null, "label_insira_um_texto_para_pesquisa");
		}
		return "";
	}

	public void pesquisaBarragem(ActionEvent e) {
		// TODO

	}

	public void pesquisaGeral(ActionEvent e) {
		// TODO
	}
}