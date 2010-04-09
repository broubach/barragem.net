package net.barragem.view.backingbean;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Jogador;
import net.barragem.scaffold.JogadoresComCorrespondenciaPrimeiroComparator;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class VincularJogadorBean extends BaseBean {

	private Jogador jogadorEmFoco;
	private String pesquisa;
	private List<Jogador> jogadores;
	private Paginavel<Jogador> paginacao;

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

	public Paginavel<Jogador> getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginavel<Jogador> paginacao) {
		this.paginacao = paginacao;
	}

	public void pesquisaJogador(ActionEvent e) {
		if (pesquisa != null && pesquisa.length() > 0) {
			List<Jogador> result = PersistenceHelper.findByNamedQuery("pesquisaJogadorForaDaListaQuery",
					getUsuarioLogado(), new StringBuilder().append("%").append(pesquisa.replace(" ", "%")).append("%")
							.toString().toUpperCase());
			if (result.size() > 0) {
				Collections.sort(result, new JogadoresComCorrespondenciaPrimeiroComparator());
				paginacao = new PaginavelSampleImpl<Jogador>(result, 5);
				jogadores = paginacao.getPagina();
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
		PersistenceHelper.persiste(Atualizacao.criaAdicionarUsuario(getUsuarioLogado(), jogadorEmFoco
				.getUsuarioCorrespondente()));
		sendMail("no-reply@barragem.net", getUsuarioLogado().getNomeCompletoCapital(), jogadorEmFoco
				.getUsuarioCorrespondente().getEmail(),
				"barragem.net - você foi adicionado(a) à lista de jogadores do(a) " + getUsuarioLogado().getNome(),
				MessageFormat.format(emailTemplateAdicaoJogador, jogadorEmFoco.getUsuarioCorrespondente()
						.getNomeCompletoCapital()));

		PersistenceHelper.persiste(jogadorEmFoco);
	}
}
