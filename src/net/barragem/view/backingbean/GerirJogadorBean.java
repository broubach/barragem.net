package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.util.JogadoresComCorrespondenciaPrimeiroComparator;
import net.barragem.util.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GerirJogadorBean extends BaseBean {
	private Usuario usuarioEmFoco;
	private String usuarioNome;
	private String pesquisa;
	private Integer tipoPesquisa = new Integer(1);
	private List<Jogador> jogadores = new ArrayList<Jogador>();

	public GerirJogadorBean() {
		usuarioEmFoco = getUsuarioLogado();
		jogadores = new ArrayList<Jogador>(usuarioEmFoco.getJogadores());
		Collections.sort(jogadores, new JogadoresComCorrespondenciaPrimeiroComparator());
	}

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public String getUsuarioNome() {
		return usuarioNome;
	}

	public void setUsuarioNome(String usuarioNome) {
		this.usuarioNome = usuarioNome;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public Integer getTipoPesquisa() {
		return tipoPesquisa;
	}

	public void setTipoPesquisa(Integer tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void adicionaJogador(ActionEvent e) {
		if (usuarioNome != null && usuarioNome.length() > 0) {
			Jogador jogador = new Jogador();
			jogador.setNome(usuarioNome);
			jogador.setUsuarioDono(usuarioEmFoco);
			usuarioEmFoco.getJogadores().add(jogador);

			PersistenceHelper.persiste(usuarioEmFoco);
		} else {
			messages.addErrorMessage(null, "label_digite_o_nome_do_novo_jogador");
		}
	}

	public void removeJogador(ActionEvent e) {
		PersistenceHelper.remove(usuarioEmFoco.getJogadores().remove(getIndex()));
	}

	public String pesquisaJogador() {
		if (tipoPesquisa.equals(new Integer(1))) {
			PesquisarBean pesquisarBean = new PesquisarBean();
			setRequestAttribute("pesquisarBean", pesquisarBean);
			return pesquisarBean.pesquisaJogador(pesquisa);
		}
		jogadores = PersistenceHelper.findByNamedQuery("pesquisaJogadorDeUsuarioQuery", getUsuarioLogado(),
				new StringBuilder().append("%").append(pesquisa).append("%").toString().toUpperCase());
		return "";
	}
}