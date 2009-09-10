package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "rodada")
public class Rodada extends BaseEntity {
	private Integer numero;
	private Boolean fechada = Boolean.FALSE;

	@ManyToOne
	private Ciclo ciclo;

	@OneToMany(mappedBy = "rodada", cascade = { CascadeType.ALL })
	private List<JogoBarragem> jogos;

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public List<JogoBarragem> getJogos() {
		if (jogos == null) {
			jogos = new ArrayList<JogoBarragem>();
		}
		return jogos;
	}

	public void setJogos(List<JogoBarragem> jogos) {
		this.jogos = jogos;
	}

	public Ciclo getCiclo() {
		return ciclo;
	}

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}

	public Boolean getFechada() {
		return fechada;
	}

	public void setFechada(Boolean fechada) {
		this.fechada = fechada;
	}

	public JogoBarragem criaJogoBarragem(Jogador jogador1, Jogador jogador2) {
		JogoBarragem jogo = new JogoBarragem();
		jogo.setRodada(this);
		jogo.setTipo(SimplesDuplasEnum.Simples);
		jogo.setPlacar(new Placar());
		jogo.getPlacar().setParciais(new ArrayList<Parcial>());

		JogadorJogoBarragem jogadorEvento1 = new JogadorJogoBarragem();
		jogadorEvento1.setEvento(jogo);
		jogadorEvento1.setJogador(jogador1);
		jogo.getJogadoresEventos().add(jogadorEvento1);

		JogadorJogoBarragem jogadorEvento2 = new JogadorJogoBarragem();
		jogadorEvento2.setEvento(jogo);
		jogadorEvento2.setJogador(jogador2);
		jogo.getJogadoresEventos().add(jogadorEvento2);

		return jogo;
	}

	public void recalculaPontuacoes() {
		Map<Jogador, Integer> pontuacaoPorJogador = calculaPontuacaoPorJogador();
		for (Jogador jogador : pontuacaoPorJogador.keySet()) {
			int indexNaLista = getCiclo().getIndexNaLista(jogador);
			int pontuacaoAtual = getCiclo().getRanking().get(indexNaLista).getPontuacao();
			int pontuacaoNova = pontuacaoAtual + pontuacaoPorJogador.get(jogador)
					- getCiclo().getPontuacaoExcedente(jogador);
			getCiclo().getRanking().get(indexNaLista).setPontuacao(pontuacaoNova);
		}
	}

	private Map<Jogador, Integer> calculaPontuacaoPorJogador() {
		Map<Jogador, Integer> pontuacaoJogadores = new HashMap<Jogador, Integer>();
		for (Jogo jogo : jogos) {
			if (jogo.getTipo().equals(SimplesDuplasEnum.Simples)) {
				if (numero.equals(new Integer(1))) {
					if (jogo.getPlacar().getWo()) {
						pontuacaoJogadores.put(jogo.getJogadorVencedorSimples(), getCiclo().getParametros()
								.getPontuacaoVencedorPrimeiraRodada());
						jogo.getJogadorBarragemVencedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoVencedorPrimeiraRodada());
					} else {
						pontuacaoJogadores.put(jogo.getJogadorVencedorSimples(), getCiclo().getParametros()
								.getPontuacaoVencedorPrimeiraRodada());
						jogo.getJogadorBarragemVencedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoVencedorPrimeiraRodada());

						pontuacaoJogadores.put(jogo.getJogadorPerdedorSimples(), getCiclo().getParametros()
								.getPontuacaoPerdedorPrimeiraRodada());
						jogo.getJogadorBarragemPerdedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoPerdedorPrimeiraRodada());
					}
				} else if (jogo.getPlacar().getWo()) {
					if (jogo.getJogadorVencedorSimples() != null) {
						pontuacaoJogadores.put(jogo.getJogadorVencedorSimples(), getCiclo().getParametros()
								.getPontuacaoVitoriaPorWo());
						jogo.getJogadorBarragemVencedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoVitoriaPorWo());
					}

				} else if (getCiclo().isPrimeiroJogadorAcima(jogo.getJogadorVencedorSimples(),
						jogo.getJogadorPerdedorSimples())) {
					if (jogo.getPlacar().isVencedorPerdeuAlgumaParcial()) {
						pontuacaoJogadores.put(jogo.getJogadorVencedorSimples(), getCiclo().getParametros()
								.getPontuacaoVencedorAcimaDoAdversarioPerdendoParciais());
						jogo.getJogadorBarragemVencedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoVencedorAcimaDoAdversarioPerdendoParciais());

						pontuacaoJogadores.put(jogo.getJogadorPerdedorSimples(), getCiclo().getParametros()
								.getPontuacaoPerdedorAbaixoDoAdversarioMarcandoParciais());
						jogo.getJogadorBarragemPerdedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoPerdedorAbaixoDoAdversarioMarcandoParciais());
					} else {
						pontuacaoJogadores.put(jogo.getJogadorVencedorSimples(), getCiclo().getParametros()
								.getPontuacaoVencedorAcimaDoAdversarioSemPerderParciais());
						jogo.getJogadorBarragemVencedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoVencedorAcimaDoAdversarioSemPerderParciais());

						pontuacaoJogadores.put(jogo.getJogadorPerdedorSimples(), getCiclo().getParametros()
								.getPontuacaoPerdedorAbaixoDoAdversarioSemMarcarParciais());
						jogo.getJogadorBarragemPerdedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoPerdedorAbaixoDoAdversarioSemMarcarParciais());
					}
				} else {
					if (jogo.getPlacar().isVencedorPerdeuAlgumaParcial()) {
						pontuacaoJogadores.put(jogo.getJogadorVencedorSimples(), getCiclo().getParametros()
								.getPontuacaoVencedorAbaixoDoAdversarioPerdendoParciais());
						jogo.getJogadorBarragemVencedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoVencedorAbaixoDoAdversarioPerdendoParciais());

						pontuacaoJogadores.put(jogo.getJogadorPerdedorSimples(), getCiclo().getParametros()
								.getPontuacaoPerdedorAcimaDoAdversarioMarcandoParciais());
						jogo.getJogadorBarragemPerdedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoPerdedorAcimaDoAdversarioMarcandoParciais());
					} else {
						pontuacaoJogadores.put(jogo.getJogadorVencedorSimples(), getCiclo().getParametros()
								.getPontuacaoVencedorAbaixoDoAdversarioSemPerderParciais());
						jogo.getJogadorBarragemVencedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoVencedorAbaixoDoAdversarioSemPerderParciais());

						pontuacaoJogadores.put(jogo.getJogadorPerdedorSimples(), getCiclo().getParametros()
								.getPontuacaoPerdedorAcimaDoAdversarioSemMarcarParciais());
						jogo.getJogadorBarragemPerdedorSimples().setPontuacaoObtida(
								getCiclo().getParametros().getPontuacaoPerdedorAcimaDoAdversarioSemMarcarParciais());
					}
				}
			}
		}
		return pontuacaoJogadores;
	}

	public List<Jogador> getJogadoresDosJogos() {
		List<Jogador> jogadoresDosJogos = new ArrayList<Jogador>();
		for (Jogo jogo : jogos) {
			for (JogadorEvento jogadorEvento : jogo.getJogadoresEventos()) {
				jogadoresDosJogos.add(jogadorEvento.getJogador());
			}
		}
		return jogadoresDosJogos;
	}

	public boolean getTodosOsJogosCompletos() {
		for (Jogo jogo : jogos) {
			if (jogo.getPlacar().getWo()) {
				continue;
			}
			boolean possuiVencedor = false;
			for (JogadorEvento jogadorEvento : jogo.getJogadoresEventos()) {
				if (((JogadorJogoBarragem) jogadorEvento).getVencedor() == null) {
					return false;
				} else if (Boolean.TRUE.equals(((JogadorJogoBarragem) jogadorEvento).getVencedor())) {
					possuiVencedor = true;
					break;
				}
			}
			if (!possuiVencedor) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ciclo.getId() == null) ? 0 : ciclo.getId().hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rodada other = (Rodada) obj;
		if (ciclo.getId() == null) {
			if (other.ciclo.getId() != null)
				return false;
		} else if (!ciclo.getId().equals(other.ciclo.getId()))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	public Integer getIndiceNaLista(Jogo jogo) {
		for (int i = 0; i < jogos.size(); i++) {
			if (jogos.get(i).equals(jogo)) {
				return i;
			}
		}
		return null;
	}

	public JogadorJogoBarragem getJogadorJogoBarragem(Jogador jogador) {
		JogadorEvento jogadorEvento = null;
		for (JogoBarragem jogo : jogos) {
			jogadorEvento = jogo.getJogadorEvento(jogador);
			if (jogadorEvento != null) {
				return (JogadorJogoBarragem) jogadorEvento;
			}
		}
		return null;
	}

	public boolean isInoperavel() {
		if (fechada || getCiclo().getRanking().size() < 2) {
			return true;
		}
		return false;
	}
}