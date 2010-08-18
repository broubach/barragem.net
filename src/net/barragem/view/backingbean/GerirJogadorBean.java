package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.JogadorBo;
import net.barragem.business.bo.UsuarioBo;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.JogadoresMaisRelevantesPrimeiroComparator;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;
import org.hibernate.exception.ConstraintViolationException;

@KeepAlive
public class GerirJogadorBean extends BaseBean {
	private Usuario usuarioEmFoco;
	private Jogador jogadorEmFoco;
	private String nome;
	private String novoNome;
	private String pesquisa;
	private String pesquisaSalva;
	private Integer tipoPesquisa = new Integer(1);
	private Paginavel<Jogador> paginacaoJogadores;

	public GerirJogadorBean() {
		prepara(null);
	}

	public void volta(ActionEvent e) {
		prepara(null);
	}

	public void prepara(Jogador foco) {
		usuarioEmFoco = getUsuarioLogado();

		List<Jogador> jogadoresSemUsuarioLogado = new ArrayList<Jogador>(usuarioEmFoco.getJogadores());
		jogadoresSemUsuarioLogado.remove(usuarioEmFoco.getJogador());
		getBo(UsuarioBo.class).carregaFotosJogadores(jogadoresSemUsuarioLogado);
		Collections.sort(jogadoresSemUsuarioLogado, new JogadoresMaisRelevantesPrimeiroComparator());

		paginacaoJogadores = new PaginavelSampleImpl<Jogador>(jogadoresSemUsuarioLogado, foco,
				paginacaoJogadores != null ? paginacaoJogadores.getPaginaAtual() : null, null);
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
		return novoNome;
	}

	public void setJogadorNome(String jogadorNome) {
		this.novoNome = jogadorNome;
	}

	public String getNovoNome() {
		return nome;
	}

	public void setNovoNome(String novoNome) {
		this.nome = novoNome;
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

	public void adicionaJogador(ActionEvent e) {
		if (validaNovo()) {
			Jogador novoJogador = getBo(JogadorBo.class).adicionaJogador(novoNome);

			prepara(novoJogador);

			addMensagemAtualizacaoComSucesso();
			novoNome = null;
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
			getBo(UsuarioBo.class).carregaFotosJogadores(resultado);
			Collections.sort(resultado, new JogadoresMaisRelevantesPrimeiroComparator());
			paginacaoJogadores = new PaginavelSampleImpl<Jogador>(resultado);
			pesquisaSalva = pesquisa;
		}
		return "";
	}

	public void preparaEdicao(ActionEvent e) {
		jogadorEmFoco = (Jogador) paginacaoJogadores.getPagina().get(getIndex());
		nome = jogadorEmFoco.getNome();
	}

	public void salvaJogador(ActionEvent e) {
		if (validaEdicao()) {
			jogadorEmFoco.setNome(nome);
			PersistenceHelper.persiste(usuarioEmFoco);
			addMensagemAtualizacaoComSucesso();
		}
	}

	private boolean validaNovo() {
		if (novoNome == null || novoNome.isEmpty()) {
			messages.addErrorMessage(null, "label_digite_o_nome_do_novo_jogador");
		} else if (getUsuarioLogado().jahPossuiJogador(novoNome)) {
			messages.addErrorMessage(null, "label_jogador_com_mesmo_nome_jah_existente");
		}
		return messages.getErrorMessages().isEmpty();
	}

	private boolean validaEdicao() {
		if (nome == null || nome.isEmpty()) {
			messages.addErrorMessage("nome", "label_true");
		} else if (getUsuarioLogado().jahPossuiJogador(nome)) {
			messages.addErrorMessage(null, "label_jogador_com_mesmo_nome_jah_existente");
		}
		return messages.getErrorMessages().isEmpty();
	}

	public void remove(ActionEvent e) {
		try {
			Jogador jogador = (Jogador) paginacaoJogadores.getPagina().get(getIndex());
			PersistenceHelper.remove(jogador);

			usuarioEmFoco.getJogadores().remove(jogador);
			PersistenceHelper.persiste(usuarioEmFoco);

			prepara(null);

			addMensagemAtualizacaoComSucesso();
		} catch (ConstraintViolationException e1) {
			messages.addErrorMessage(null,
					"label_jogador_nao_pode_ser_removido_pois_eh_utilizado_em_algum_jogo_barragem");
		}
	}

	public void limpaFiltro(ActionEvent e) {
		pesquisaSalva = null;

		prepara(null);
	}

	public void preparaVinculo(ActionEvent e) {
		Jogador jogador = (Jogador) paginacaoJogadores.getPagina().get(getIndex());
		VincularJogadorBean vincularJogadorBean = new VincularJogadorBean();
		vincularJogadorBean.setJogadorEmFoco(jogador);
		vincularJogadorBean.setPesquisa(jogador.getNome());
		setRequestAttribute("vincularJogadorBean", vincularJogadorBean);
	}
}