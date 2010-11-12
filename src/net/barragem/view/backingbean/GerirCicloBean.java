package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.JogadorBo;
import net.barragem.business.bo.RelatorioBo;
import net.barragem.business.bo.UsuarioBo;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.JogadorJogoBarragem;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Rodada;
import net.barragem.scaffold.MessageBundleUtils;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.JogadorSelecionavelDto;
import net.barragem.view.backingbean.componentes.RelatorioRankingDto;
import net.barragem.view.backingbean.componentes.RelatorioRodadasDto;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GerirCicloBean extends BaseBean {

	private Barragem barragemEmFoco;
	private Ciclo cicloEmFoco;
	private CicloJogador cicloJogadorEmFoco;
	private List<CicloJogador> cicloJogadoresRemovidos;
	private List<JogadorSelecionavelDto> jogadoresSelecionaveis;
	private String novoNome;
	private String filtroJogador;
	private String jogadorNome;
	private boolean habilitado;

	public String getNovoNome() {
		return novoNome;
	}

	public void setNovoNome(String novoNome) {
		this.novoNome = novoNome;
	}

	public Barragem getBarragemEmFoco() {
		return barragemEmFoco;
	}

	public void setBarragemEmFoco(Barragem barragem) {
		this.barragemEmFoco = barragem;
	}

	public Ciclo getCicloEmFoco() {
		return cicloEmFoco;
	}

	public void setCicloEmFoco(Ciclo ciclo) {
		this.cicloEmFoco = ciclo;
	}

	public CicloJogador getCicloJogadorEmFoco() {
		return cicloJogadorEmFoco;
	}

	public void setCicloJogadorEmFoco(CicloJogador cicloJogadorEmFoco) {
		this.cicloJogadorEmFoco = cicloJogadorEmFoco;
	}

	public List<JogadorSelecionavelDto> getJogadoresSelecionaveis() {
		return jogadoresSelecionaveis;
	}

	public void setJogadoresSelecionaveis(List<JogadorSelecionavelDto> jogadoresSelecionaveis) {
		this.jogadoresSelecionaveis = jogadoresSelecionaveis;
	}

	public String getFiltroJogador() {
		return filtroJogador;
	}

	public void setFiltroJogador(String filtroJogador) {
		this.filtroJogador = filtroJogador;
	}

	public String getJogadorNome() {
		return jogadorNome;
	}

	public void setJogadorNome(String jogadorNome) {
		this.jogadorNome = jogadorNome;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public void editaCiclo(ActionEvent e) {
		barragemEmFoco = PersistenceHelper.findByPk(Barragem.class, barragemEmFoco.getId(), "ciclos");
		if (getIndex() != -1) {
			Ciclo ciclo = barragemEmFoco.getCiclos().get(getIndex());
			cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, ciclo.getId(), "ranking", "rodadas");
		} else {
			cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, cicloEmFoco.getId(), "ranking", "rodadas");
		}
		cicloJogadoresRemovidos = new ArrayList<CicloJogador>();
	}

	public void preparaRenomea(ActionEvent e) {
		novoNome = cicloEmFoco.getNome();
	}

	public void carregaUltimoCiclo(Barragem barragem) {
		setBarragemEmFoco(barragem);
		setCicloEmFoco(barragem.getCiclos().get(barragem.getCiclos().size() - 1));
		setCicloEmFoco((Ciclo) PersistenceHelper.findByPk(Ciclo.class, getCicloEmFoco().getId(), "ranking", "rodadas"));
		cicloJogadoresRemovidos = new ArrayList<CicloJogador>();
	}

	public void adicionaJogadores(ActionEvent e) {
		boolean salvaCiclo = false;
		JogadorSelecionavelDto jogadorSelecionavel = null;
		for (Iterator<JogadorSelecionavelDto> it = jogadoresSelecionaveis.iterator(); it.hasNext();) {
			jogadorSelecionavel = it.next();
			if (!jogadorSelecionavel.isSelecionado()) {
				continue;
			} else {
				salvaCiclo = true;
			}
			CicloJogador cicloJogador = new CicloJogador();
			if (!cicloEmFoco.getRanking().isEmpty()) {
				cicloJogador
						.setRanking(cicloEmFoco.getRanking().get(cicloEmFoco.getRanking().size() - 1).getRanking() + 1);
			} else {
				cicloJogador.setRanking(1);
			}
			cicloJogador.setPontuacao(0);
			cicloJogador.setCiclo(cicloEmFoco);
			cicloJogador.setJogador(jogadorSelecionavel.getJogador());
			cicloEmFoco.getRanking().add(cicloJogador);
			it.remove();
		}

		if (salvaCiclo) {
			salvaCiclo();
			addMensagemAtualizacaoComSucesso();
		}
	}

	public void pesquisaJogadorPrimeiraVez(ActionEvent e) {
		filtroJogador = null;
		pesquisaJogador(e);
	}

	public void pesquisaJogador(ActionEvent e) {
		jogadoresSelecionaveis = new ArrayList<JogadorSelecionavelDto>();
		List<Jogador> todosJogadores = PersistenceHelper.findByNamedQuery("jogadoresPorUsuarioDonoQuery",
				filtroJogador != null ? filtroJogador + "%" : "%", getUsuarioLogado().getId());
		JogadorSelecionavelDto jogadorSelecionavel = null;
		for (Jogador jogador : todosJogadores) {
			if (!cicloEmFoco.getJogadoresDoRanking().contains(jogador)) {
				jogadorSelecionavel = new JogadorSelecionavelDto();
				jogadorSelecionavel.setJogador(jogador);
				jogadoresSelecionaveis.add(jogadorSelecionavel);
			}
		}
	}

	private void salvaCiclo() {
		for (Rodada rodada : cicloEmFoco.getRodadas()) {
			PersistenceHelper.initialize("jogos", rodada);
		}
		for (CicloJogador cicloJogador : cicloJogadoresRemovidos) {
			if (!cicloEmFoco.existeJogoComJogador(cicloJogador.getJogador())) {
				PersistenceHelper.remove(cicloJogador);
			} else {
				messages.addErrorMessage(null, "error_jogador_nao_pode_ser_excluido");
				cicloEmFoco.getRanking().add(cicloJogador);
				cicloJogador.setCiclo(cicloEmFoco);
			}
		}
		cicloEmFoco.recalculaRanking();
		PersistenceHelper.persiste(cicloEmFoco);
		cicloJogadoresRemovidos = new ArrayList<CicloJogador>();
	}

	public void salvaCiclo(ActionEvent e) {
		if (valida()) {
			cicloEmFoco.setNome(novoNome);
			salvaCiclo();
			editaCiclo(e);
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

	public void removeJogador(ActionEvent e) {
		cicloEmFoco.getRanking().get(getIndex()).setCiclo(null);
		cicloJogadoresRemovidos.add(cicloEmFoco.getRanking().get(getIndex()));
		cicloEmFoco.getRanking().remove(getIndex());
		salvaCiclo();
		addMensagemAtualizacaoComSucesso();
	}

	public void editaRodada(ActionEvent e) {
		GerirRodadaBean rodadaBean = new GerirRodadaBean();
		rodadaBean.carregaRodada(cicloEmFoco, getIndex());

		setRequestAttribute("gerirRodadaBean", rodadaBean);
	}

	public void criaNovoCiclo(ActionEvent e) {
		Integer proximoNome = barragemEmFoco.getCiclos().get(barragemEmFoco.getCiclos().size() - 1)
				.getNomeAlternativoBaseadoEmAno() + 1;
		PersistenceHelper.persiste(barragemEmFoco.criaCicloERodada(proximoNome));

		GerirCicloBean cicloBean = new GerirCicloBean();
		cicloBean.carregaUltimoCiclo(barragemEmFoco);

		setRequestAttribute("gerirCicloBean", cicloBean);
		addMensagemAtualizacaoComSucesso();
	}

	public void preparaCicloJogador(ActionEvent e) {
		cicloJogadorEmFoco = cicloEmFoco.getRanking().get(getIndex());
		habilitado = cicloJogadorEmFoco.getHabilitado();
	}

	public void salvaCicloJogador(ActionEvent e) {
		cicloJogadorEmFoco.setHabilitado(habilitado);
		PersistenceHelper.persiste(cicloJogadorEmFoco);
		addMensagemAtualizacaoComSucesso();
	}

	public List<String> autocomplete(Object suggest) {
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();

		for (Jogador jogador : getUsuarioLogado().getJogadores()) {
			if ((jogador.getNome() != null && jogador.getNome().toLowerCase().indexOf(pref.toLowerCase()) == 0 && !cicloEmFoco
					.getJogadoresDoRanking().contains(jogador))
					|| "".equals(pref)) {
				result.add(jogador.getNome());
			}
		}

		return result;
	}

	public void adicionaJogador(ActionEvent e) {
		if (validaNovo()) {
			getBo(JogadorBo.class).adicionaJogador(jogadorNome);
			filtroJogador = null;
			pesquisaJogador(e);

			addMensagemAtualizacaoComSucesso();
			jogadorNome = null;
		}
	}

	private boolean validaNovo() {
		if (jogadorNome == null || jogadorNome.isEmpty()) {
			messages.addErrorMessage(null, "label_digite_o_nome_do_novo_jogador");
		} else if (getUsuarioLogado().jahPossuiJogador(jogadorNome)) {
			messages.addErrorMessage(null, "label_jogador_com_mesmo_nome_jah_existente");
		}
		return messages.getErrorMessages().isEmpty();
	}

	public void exibeRelatorioRanking(ActionEvent e) {
		List<RelatorioRankingDto> reportSource = new ArrayList<RelatorioRankingDto>();
		RelatorioRankingDto dto = null;
		for (CicloJogador cicloJogador : cicloEmFoco.getRanking()) {
			if (!cicloJogador.getHabilitado()) {
				continue;
			}
			dto = new RelatorioRankingDto();
			dto.setJogadorNome(cicloJogador.getJogador().getNome());
			dto.setPontuacao(cicloJogador.getPontuacao());
			dto.setRanking(cicloJogador.getRanking());
			if (cicloJogador.getJogador().getUsuarioCorrespondente() != null
					&& cicloJogador.getJogador().getUsuarioCorrespondente().getPerfil() != null
					&& cicloJogador.getJogador().getUsuarioCorrespondente().getPerfil().getHash() != null) {
				dto.setJogadorHash(cicloJogador.getJogador().getUsuarioCorrespondente().getPerfil().getHash());
			} else {
				dto.setJogadorHash("default");
			}
			reportSource.add(dto);
		}

		Map<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("CICLO", cicloEmFoco.getNome());
		parametros.put("LOCAL", cicloEmFoco.getBarragem().getLocal());
		parametros.put("CATEGORIA", MessageBundleUtils.getInstance().get(
				cicloEmFoco.getBarragem().getCategoria().getNome()));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		parametros.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("GMT-3"));

		flushReport("/jasper/ranking.jasper", "ranking.pdf", parametros, reportSource);
	}

	public void exibeRelatorioRodadas(ActionEvent e) {
		getBo(UsuarioBo.class).carregaFotosJogadores(cicloEmFoco.getJogadoresDoRanking());
		getBo(RelatorioBo.class).inicializaRodadas(null, null, cicloEmFoco.getRodadas());
		List<RelatorioRodadasDto> reportSource = new ArrayList<RelatorioRodadasDto>();
		RelatorioRodadasDto dto = null;
		for (Rodada rodada : cicloEmFoco.getRodadas()) {
			for (JogoBarragem jogoBarragem : rodada.getJogosOrdenados()) {
				dto = new RelatorioRodadasDto();
				dto.setRodada(rodada.getNumero() + MessageBundleUtils.getInstance().get("label_rodada_posfixo"));
				dto.setData(jogoBarragem.getData());
				dto.setHora(jogoBarragem.getHora());
				dto.setPontuacaoVencedor(((JogadorJogoBarragem) jogoBarragem.getJogadoresEventosOrdenados().get(0))
						.getPontuacaoObtida());
				dto.setJogadorVencedorNome(jogoBarragem.getJogadoresEventosOrdenados().get(0).getJogador().getNome());
				if (jogoBarragem.getJogadoresEventosOrdenados().get(0).getJogador().getUsuarioCorrespondente() != null
						&& jogoBarragem.getJogadoresEventosOrdenados().get(0).getJogador().getUsuarioCorrespondente()
								.getPerfil() != null
						&& jogoBarragem.getJogadoresEventosOrdenados().get(0).getJogador().getUsuarioCorrespondente()
								.getPerfil().getHash() != null) {
					dto.setJogadorVencedorHash(jogoBarragem.getJogadoresEventosOrdenados().get(0).getJogador()
							.getUsuarioCorrespondente().getPerfil().getHash());
				} else {
					dto.setJogadorVencedorHash("default");
				}
				dto.setPontuacaoPerdedor(((JogadorJogoBarragem) jogoBarragem.getJogadoresEventosOrdenados().get(1))
						.getPontuacaoObtida());
				dto.setJogadorPerdedorNome(jogoBarragem.getJogadoresEventosOrdenados().get(1).getJogador().getNome());
				if (jogoBarragem.getJogadoresEventosOrdenados().get(1).getJogador().getUsuarioCorrespondente() != null
						&& jogoBarragem.getJogadoresEventosOrdenados().get(1).getJogador().getUsuarioCorrespondente()
								.getPerfil() != null
						&& jogoBarragem.getJogadoresEventosOrdenados().get(1).getJogador().getUsuarioCorrespondente()
								.getPerfil().getHash() != null) {
					dto.setJogadorPerdedorHash(jogoBarragem.getJogadoresEventosOrdenados().get(1).getJogador()
							.getUsuarioCorrespondente().getPerfil().getHash());
				} else {
					dto.setJogadorPerdedorHash("default");
				}
				if (jogoBarragem.getPlacar() != null && jogoBarragem.getPlacar().getWo()) {
					dto.setPlacar("WO");
				} else {
					dto.setPlacar(jogoBarragem.getPlacar() != null && jogoBarragem.getPlacar().getParciais() != null
							&& jogoBarragem.getPlacar().getParciais().size() > 0 ? jogoBarragem.getPlacar().toString()
							: "_/_ _/_ _/_");
				}
				reportSource.add(dto);
			}
		}

		Map<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("CICLO", cicloEmFoco.getNome());
		parametros.put("LOCAL", cicloEmFoco.getBarragem().getLocal());
		parametros.put("CATEGORIA", MessageBundleUtils.getInstance().get(
				cicloEmFoco.getBarragem().getCategoria().getNome()));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		parametros.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("GMT-3"));

		flushReport("/jasper/rodadas.jasper", "rodadas.pdf", parametros, reportSource);
	}
}