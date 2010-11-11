package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.UsuarioBo;
import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.JogadorJogoBarragem;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Rodada;
import net.barragem.scaffold.MessageBundleUtils;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.EventoMaisRecenteComparator;
import net.barragem.view.backingbean.componentes.JogadorEventoComparatorVencedorPrimeiro;
import net.barragem.view.backingbean.componentes.RelatorioRankingDto;
import net.barragem.view.backingbean.componentes.RelatorioRodadasDto;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirPainelBarragemBean extends BaseBean {

	private Ciclo cicloEmFoco;
	private Integer startIndex = new Integer(0);
	private Integer endIndex = new Integer(1);
	private Integer numeroRodada;

	private List<Rodada> rodadas;
	private List<CicloJogador> ranking;
	private List<Ciclo> ciclos;

	public ExibirPainelBarragemBean() {
		ciclos = PersistenceHelper.findByNamedQuery("ciclosDeBarragemQuery", getId());

		cicloEmFoco = (Ciclo) PersistenceHelper.findByNamedQuery("ultimoCicloPorBarragemQuery", getId()).get(0);

		PersistenceHelper.initialize("rodadas", cicloEmFoco);
		rodadas = new ArrayList<Rodada>(cicloEmFoco.getRodadas());
		Collections.reverse(rodadas);
		inicializaRodadas();

		PersistenceHelper.initialize("ranking", cicloEmFoco);
		ranking = cicloEmFoco.getRanking();
	}

	private void inicializaRodadas() {
		inicializaRodadas(startIndex, endIndex);
	}

	private void inicializaRodadas(Integer startIndex, Integer endIndex) {
		if (startIndex == null || endIndex == null) {
			startIndex = 0;
			endIndex = rodadas.size() - 1;
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (i < rodadas.size()) {
				PersistenceHelper.initialize("bonuses", rodadas.get(i));
				PersistenceHelper.initialize("jogos", rodadas.get(i));
				for (Jogo jogoBarragem : rodadas.get(i).getJogos()) {
					PersistenceHelper.initialize("parciais", jogoBarragem.getPlacar());
					jogoBarragem.setJogadoresEventosOrdenados(new ArrayList<JogadorEvento>(jogoBarragem
					        .getJogadoresEventos()));
					Collections.sort(jogoBarragem.getJogadoresEventosOrdenados(),
					        new JogadorEventoComparatorVencedorPrimeiro());
				}
				rodadas.get(i).setJogosOrdenados(new ArrayList<JogoBarragem>(rodadas.get(i).getJogos()));
				Collections.sort(rodadas.get(i).getJogosOrdenados(), new EventoMaisRecenteComparator());
			}
		}
	}

	public Ciclo getCicloEmFoco() {
		return cicloEmFoco;
	}

	public void setCicloEmFoco(Ciclo cicloEmFoco) {
		this.cicloEmFoco = cicloEmFoco;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer beginIndex) {
		this.startIndex = beginIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public Integer getNumeroRodada() {
		return numeroRodada;
	}

	public void setNumeroRodada(Integer numeroRodada) {
		this.numeroRodada = numeroRodada;
	}

	public List<Rodada> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<Rodada> rodadas) {
		this.rodadas = rodadas;
	}

	public List<CicloJogador> getRanking() {
		return ranking;
	}

	public void setRanking(List<CicloJogador> ranking) {
		this.ranking = ranking;
	}

	public List<Ciclo> getCiclos() {
		return ciclos;
	}

	public void setCiclos(List<Ciclo> ciclos) {
		this.ciclos = ciclos;
	}

	public void visualizaCiclo(ActionEvent e) {
		if (getIndex() != -1) {
			Ciclo ciclo = cicloEmFoco.getBarragem().getCiclos().get(getIndex());
			cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, ciclo.getId(), "ranking", "rodadas");
		} else {
			cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, cicloEmFoco.getId(), "ranking", "rodadas");
		}
		PersistenceHelper.initialize("ciclos", cicloEmFoco.getBarragem());
	}

	public String visualizaRodada() {
		if (numeroRodada != null && numeroRodada > 0 && numeroRodada <= cicloEmFoco.getRodadas().size()) {
			startIndex = cicloEmFoco.getRodadas().size() - numeroRodada;
			endIndex = startIndex + 1;
		} else {
			numeroRodada = cicloEmFoco.getRodadas().size();
			startIndex = 0;
			endIndex = 1;
		}
		inicializaRodadas();
		refreshView();

		return null;
	}

	public void exibeRelatorioRanking(ActionEvent e) {
		List<RelatorioRankingDto> reportSource = new ArrayList<RelatorioRankingDto>();
		RelatorioRankingDto dto = null;
		for (CicloJogador cicloJogador : ranking) {
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
		parametros.put("CATEGORIA",
		        MessageBundleUtils.getInstance().get(cicloEmFoco.getBarragem().getCategoria().getNome()));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		parametros.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("GMT-3"));

		flushReport("/jasper/ranking.jasper", "ranking.pdf", parametros, reportSource);
	}

	public void exibeRelatorioRodadas(ActionEvent e) {
		getBo(UsuarioBo.class).carregaFotosJogadores(cicloEmFoco.getJogadoresDoRanking());
		inicializaRodadas(null, null);
		List<RelatorioRodadasDto> reportSource = new ArrayList<RelatorioRodadasDto>();
		RelatorioRodadasDto dto = null;
		for (Rodada rodada : rodadas) {
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
		parametros.put("CATEGORIA",
		        MessageBundleUtils.getInstance().get(cicloEmFoco.getBarragem().getCategoria().getNome()));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		parametros.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("GMT-3"));

		flushReport("/jasper/rodadas.jasper", "rodadas.pdf", parametros, reportSource);
	}
}