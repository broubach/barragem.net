package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.JogadoresComCorrespondenciaPrimeiroComparator;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;
import org.hibernate.exception.ConstraintViolationException;

@KeepAlive
public class GerirJogadorBean extends BaseBean {
	private Usuario usuarioEmFoco;
	private Jogador jogadorEmFoco;
	private String novoNome;
	private String jogadorNome;
	private String pesquisa;
	private String pesquisaSalva;
	private Integer tipoPesquisa = new Integer(1);
	private ListDataModel jogadores;
	private Paginavel<Jogador> paginacaoJogadores;

	public GerirJogadorBean() {
		usuarioEmFoco = getUsuarioLogado();
		List<Jogador> jogadoresSemUsuarioLogado = new ArrayList<Jogador>(usuarioEmFoco.getJogadores());
		jogadoresSemUsuarioLogado.remove(usuarioEmFoco.getJogador());
		Collections.sort(jogadoresSemUsuarioLogado, new JogadoresComCorrespondenciaPrimeiroComparator());
		paginacaoJogadores = new PaginavelSampleImpl<Jogador>(jogadoresSemUsuarioLogado);
		jogadores = new ListDataModel(paginacaoJogadores.getPagina());
	}

	public Paginavel<Jogador> getPaginacaoJogadores() {
		return paginacaoJogadores;
	}

	public void setPaginacaoJogadores(Paginavel<Jogador> paginacaoJogadores) {
		this.paginacaoJogadores = paginacaoJogadores;
	}

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public String getJogadorNome() {
		return jogadorNome;
	}

	public void setJogadorNome(String jogadorNome) {
		this.jogadorNome = jogadorNome;
	}

	public String getNovoNome() {
		return novoNome;
	}

	public void setNovoNome(String novoNome) {
		this.novoNome = novoNome;
	}

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

	public String getPesquisaSalva() {
		return pesquisaSalva;
	}

	public void setPesquisaSalva(String pesquisaSalva) {
		this.pesquisaSalva = pesquisaSalva;
	}

	public Integer getTipoPesquisa() {
		return tipoPesquisa;
	}

	public void setTipoPesquisa(Integer tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
	}

	public void setJogadores(ListDataModel jogadores) {
		this.jogadores = jogadores;
	}

	public ListDataModel getJogadores() {
		return jogadores;
	}

	public void adicionaJogador(ActionEvent e) {
		if (jogadorNome != null && jogadorNome.length() > 0) {
			Jogador jogador = new Jogador();
			jogador.setNome(jogadorNome);
			jogador.setUsuarioDono(usuarioEmFoco);
			usuarioEmFoco.getJogadores().add(jogador);

			PersistenceHelper.persiste(usuarioEmFoco);

			List<Jogador> jogadoresSemUsuarioLogado = new ArrayList<Jogador>(usuarioEmFoco.getJogadores());
			jogadoresSemUsuarioLogado.remove(usuarioEmFoco.getJogador());
			Collections.sort(jogadoresSemUsuarioLogado, new JogadoresComCorrespondenciaPrimeiroComparator());
			paginacaoJogadores = new PaginavelSampleImpl<Jogador>(jogadoresSemUsuarioLogado, jogador);
			jogadores = new ListDataModel(paginacaoJogadores.getPagina());

			addMensagemAtualizacaoComSucesso();
			jogadorNome = null;
		} else {
			messages.addErrorMessage(null, "label_digite_o_nome_do_novo_jogador");
		}
	}

	public String pesquisaJogador() {
		if (tipoPesquisa.equals(new Integer(1))) {
			PesquisarBean pesquisarBean = new PesquisarBean();
			setRequestAttribute("pesquisarBean", pesquisarBean);
			pesquisaSalva = null;
			return pesquisarBean.pesquisaJogador(pesquisa);
		}
		if (pesquisa == null || pesquisa.length() == 0) {
			messages.addErrorMessage(null, "label_insira_um_texto_para_pesquisa");
			return "";
		}
		List<Jogador> resultado = PersistenceHelper.findByNamedQuery("pesquisaJogadorDeUsuarioQuery",
				getUsuarioLogado(), new StringBuilder().append("%").append(pesquisa).append("%").toString()
						.toUpperCase());
		if (resultado.isEmpty()) {
			messages.addInfoMessage("label_nenhum_resultado_encontrado", "label_nenhum_resultado_encontrado");
		} else {
			Collections.sort(resultado, new JogadoresComCorrespondenciaPrimeiroComparator());
			paginacaoJogadores = new PaginavelSampleImpl<Jogador>(resultado);
			jogadores = new ListDataModel(paginacaoJogadores.getPagina());
			pesquisaSalva = pesquisa;
		}
		return "";
	}

	public void preparaEdicao(ActionEvent e) {
		jogadorEmFoco = (Jogador) getJogadores().getRowData();
		novoNome = jogadorEmFoco.getNome();
	}

	public void salvaJogador(ActionEvent e) {
		if (valida()) {
			jogadorEmFoco.setNome(novoNome);
			PersistenceHelper.persiste(usuarioEmFoco);
			addMensagemAtualizacaoComSucesso();
		}
	}

	private boolean valida() {
		if (novoNome == null || novoNome.isEmpty()) {
			messages.addErrorMessage("nome", "label_true");
			return false;
		}
		return true;
	}

	public void remove(ActionEvent e) {
		try {
			Jogador jogador = (Jogador) getJogadores().getRowData();
			PersistenceHelper.remove(jogador);

			usuarioEmFoco.getJogadores().remove(jogador);
			PersistenceHelper.persiste(usuarioEmFoco);

			paginacaoJogadores.getSourceList().remove(jogador);
			paginacaoJogadores.pesquisaPaginavel(paginacaoJogadores.getPaginaAtual());
			addMensagemAtualizacaoComSucesso();
		} catch (ConstraintViolationException e1) {
			messages.addErrorMessage(null,
					"label_jogador_nao_pode_ser_removido_pois_eh_utilizado_em_algum_jogo_barragem");
		}
	}

	public void limpaFiltro(ActionEvent e) {
		pesquisaSalva = null;

		List<Jogador> jogadoresSemUsuarioLogado = new ArrayList<Jogador>(usuarioEmFoco.getJogadores());
		jogadoresSemUsuarioLogado.remove(usuarioEmFoco.getJogador());
		paginacaoJogadores = new PaginavelSampleImpl<Jogador>(jogadoresSemUsuarioLogado);
		jogadores = new ListDataModel(paginacaoJogadores.getPagina());
	}

	public void preparaVinculo(ActionEvent e) {
		Jogador jogador = (Jogador) getJogadores().getRowData();
		VincularJogadorBean vincularJogadorBean = new VincularJogadorBean();
		vincularJogadorBean.setJogadorEmFoco(jogador);
		vincularJogadorBean.setPesquisa(jogador.getNome());
		setRequestAttribute("vincularJogadorBean", vincularJogadorBean);
	}
}