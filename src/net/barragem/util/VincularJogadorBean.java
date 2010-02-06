package net.barragem.util;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Jogador;
import net.barragem.view.backingbean.BaseBean;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class VincularJogadorBean extends BaseBean {

	private Jogador jogadorEmFoco;
	private String pesquisa;
	private List<Jogador> jogadores;

	public Jogador getJogadorEmFoco() {
		return jogadorEmFoco;
	}

	public void setJogadorEmFoco(Jogador jogadorEmFoco) {
		this.jogadorEmFoco = jogadorEmFoco;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public void pesquisaJogador(ActionEvent e) {
		if (pesquisa != null && pesquisa.length() > 0) {
			jogadores = PersistenceHelper.findByNamedQuery("pesquisaJogadorForaDaListaQuery", getUsuarioLogado(),
					new StringBuilder().append("%").append(pesquisa).append("%").toString().toUpperCase());
			if (jogadores.size() > 0) {
				return;
			}

			messages.addInfoMessage("label_nenhum_resultado_encontrado", "label_nenhum_resultado_encontrado");
		} else {
			messages.addErrorMessage(null, "label_insira_um_texto_para_pesquisa");
		}
	}

	public void vinculaJogador(ActionEvent e) {
		Jogador jogador = PersistenceHelper.findByPk(Jogador.class, getId());

		jogadorEmFoco.setNome(jogador.getNome());
		jogadorEmFoco.setUsuarioCorrespondente(jogador.getUsuarioCorrespondente());

		PersistenceHelper.persiste(jogadorEmFoco);
	}
}
