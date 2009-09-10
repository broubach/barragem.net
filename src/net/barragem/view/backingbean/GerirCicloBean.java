package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Rodada;
import net.barragem.util.MessageUtils;
import net.barragem.util.PersistenceHelper;
import net.barragem.view.dto.JogadorSelecionavelDto;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GerirCicloBean extends BaseBean {

	private Barragem barragemEmFoco;
	private Ciclo cicloEmFoco;
	private List<CicloJogador> cicloJogadoresRemovidos;
	private List<JogadorSelecionavelDto> jogadoresSelecionaveis;
	private Boolean todos;

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

	public List<JogadorSelecionavelDto> getJogadoresSelecionaveis() {
		return jogadoresSelecionaveis;
	}

	public void setJogadoresSelecionaveis(List<JogadorSelecionavelDto> jogadoresSelecionaveis) {
		this.jogadoresSelecionaveis = jogadoresSelecionaveis;
	}

	public Boolean getTodos() {
		return todos;
	}

	public void setTodos(Boolean todos) {
		this.todos = todos;
	}

	public void editaCiclo(ActionEvent e) {
		barragemEmFoco = PersistenceHelper.findByPk(Barragem.class, barragemEmFoco.getId(), "ciclos");
		Ciclo ciclo = barragemEmFoco.getCiclos().get(getIndex());
		cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, ciclo.getId(), "ranking", "rodadas");
		cicloJogadoresRemovidos = new ArrayList<CicloJogador>();
	}

	public void salvaCiclo(ActionEvent e) {
		for (Rodada rodada : cicloEmFoco.getRodadas()) {
			PersistenceHelper.initialize("jogos", rodada);
		}
		for (CicloJogador cicloJogador : cicloJogadoresRemovidos) {
			if (!cicloEmFoco.existeJogoComJogador(cicloJogador.getJogador())) {
				PersistenceHelper.remove(cicloJogador);
			} else {
				cicloEmFoco.getRanking().add(cicloJogador);
				cicloJogador.setCiclo(cicloEmFoco);
			}
		}
		cicloEmFoco.recalculaRanking();
		PersistenceHelper.persiste(cicloEmFoco);
		cicloJogadoresRemovidos = new ArrayList<CicloJogador>();
	}

	public void carregaUltimoCiclo(Barragem barragem) {
		setBarragemEmFoco(barragem);
		setCicloEmFoco(barragem.getCiclos().get(barragem.getCiclos().size() - 1));
		setCicloEmFoco((Ciclo) PersistenceHelper.findByPk(Ciclo.class, getCicloEmFoco().getId(), "ranking", "rodadas"));
		cicloJogadoresRemovidos = new ArrayList<CicloJogador>();
	}

	public void adicionaJogadores(ActionEvent e) {
		JogadorSelecionavelDto jogadorSelecionavel = null;
		for (Iterator<JogadorSelecionavelDto> it = jogadoresSelecionaveis.iterator(); it.hasNext();) {
			jogadorSelecionavel = it.next();
			if (!jogadorSelecionavel.isSelecionado()) {
				continue;
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
	}

	public void removeJogador(ActionEvent e) {
		cicloEmFoco.getRanking().get(getIndex()).setCiclo(null);
		cicloJogadoresRemovidos.add(cicloEmFoco.getRanking().get(getIndex()));
		cicloEmFoco.getRanking().remove(getIndex());
	}

	public String getMensagemRodadasSugeridas() {
		int rodadasSugeridas = cicloEmFoco.getParametros().getDuracaoEmMeses() * 3;
		return MessageUtils.getInstance().get(
				"label_rodadas_sugeridas",
				new String[] { String.valueOf(cicloEmFoco.getParametros().getDuracaoEmMeses()),
						String.valueOf(rodadasSugeridas) });
	}

	public void editaRodada(ActionEvent e) {
		GerirRodadaBean rodadaBean = new GerirRodadaBean();
		rodadaBean.carregaRodada(cicloEmFoco, getIndex());

		setRequestAttribute("gerirRodadaBean", rodadaBean);
	}

	public void criaJogadoresSelecionaveis(ActionEvent e) {
		jogadoresSelecionaveis = new ArrayList<JogadorSelecionavelDto>();
		List<Jogador> todosJogadores = PersistenceHelper.findByNamedQuery("jogadoresPorUsuarioDonoQuery",
				getUsuarioLogado().getId());
		JogadorSelecionavelDto jogadorSelecionavel = null;
		for (Jogador jogador : todosJogadores) {
			if (!cicloEmFoco.getJogadoresDoRanking().contains(jogador)) {
				jogadorSelecionavel = new JogadorSelecionavelDto();
				jogadorSelecionavel.setJogador(jogador);
				jogadoresSelecionaveis.add(jogadorSelecionavel);
			}
		}
		todos = Boolean.FALSE;
	}

	public void selecionaTodos(ActionEvent e) {
		JogadorSelecionavelDto jogadorSelecionavel = null;
		for (Iterator<JogadorSelecionavelDto> it = jogadoresSelecionaveis.iterator(); it.hasNext();) {
			jogadorSelecionavel = it.next();
			jogadorSelecionavel.setSelecionado(todos);
		}
	}
}