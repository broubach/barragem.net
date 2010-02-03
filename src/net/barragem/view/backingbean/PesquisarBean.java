package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;
import net.barragem.util.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class PesquisarBean extends BaseBean {

	private List<Jogador> jogadores;
	private List<Barragem> barragens;
	private List<Object> geral;
	private String pesquisa;

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

	public String pesquisaJogador(String pesquisa) {
		if (pesquisa != null && pesquisa.length() > 0) {
			jogadores = PersistenceHelper.findByNamedQuery("pesquisaJogadorQuery", new StringBuilder().append("%")
					.append(pesquisa).append("%").toString().toUpperCase());
			if (jogadores.size() > 0) {
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