package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import net.barragem.scaffold.PersistenceHelper;

@Entity
@Table(name = "ciclo")
@NamedQueries( {
		@NamedQuery(name = "ultimoCicloPorBarragemQuery", query = "select c from Barragem b join b.ciclos c where b.id = :idBarragem and c.id = (select max(ciclo.id) from Ciclo ciclo where ciclo.barragem.id = b.id)"),
		@NamedQuery(name = "ciclosDeBarragemQuery", query = "select c from Barragem b join b.ciclos c where b.id = :idBarragem"), })
public class Ciclo extends BaseEntity {

	@ManyToOne
	private Barragem barragem;
	private ParametroCiclo parametros;
	private Date dataInicio;
	private String nome;
	private Integer nomeAlternativoBaseadoEmAno;

	@OneToMany(mappedBy = "ciclo", cascade = { CascadeType.ALL })
	@OrderBy(value = "ranking")
	private List<CicloJogador> ranking;

	@OneToMany(mappedBy = "ciclo", cascade = { CascadeType.ALL })
	private List<Rodada> rodadas;

	public Barragem getBarragem() {
		return barragem;
	}

	public void setBarragem(Barragem barragem) {
		this.barragem = barragem;
	}

	public ParametroCiclo getParametros() {
		return parametros;
	}

	public void setParametros(ParametroCiclo parametros) {
		this.parametros = parametros;
	}

	public List<CicloJogador> getRanking() {
		return ranking;
	}

	public void setRanking(List<CicloJogador> ranking) {
		this.ranking = ranking;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getNome() {
		if (nome == null) {
			return String.valueOf(nomeAlternativoBaseadoEmAno);
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNomeAlternativoBaseadoEmAno(Integer i) {
		this.nomeAlternativoBaseadoEmAno = i;
	}

	public Integer getNomeAlternativoBaseadoEmAno() {
		return nomeAlternativoBaseadoEmAno;
	}

	public List<Rodada> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<Rodada> rodadas) {
		this.rodadas = rodadas;
	}

	public int sorteiaBaseadoNoRaio(int limiteRaio) {
		double sorteio = Math.random();
		int raio = getParametros().getRaioParaSorteioDeJogosNoRanking() < limiteRaio ? getParametros()
				.getRaioParaSorteioDeJogosNoRanking() : limiteRaio - 1;
		double range = 1.0d / raio;
		int i = 1;
		while (sorteio > range * i) {
			i++;
		}
		return i;
	}

	public Rodada adicionaRodada() {
		Rodada rodada = new Rodada();
		rodada.setNumero(new Integer(rodadas.size() + 1));
		rodada.setCiclo(this);
		rodadas.add(rodada);
		return rodada;
	}

	public void recalculaRanking() {
		Collections.sort(ranking, new Comparator<CicloJogador>() {
			public int compare(CicloJogador cicloJogador1, CicloJogador cicloJogador2) {
				if (cicloJogador1.getPontuacao() != null) {
					int comparacao = cicloJogador1.getPontuacao().compareTo(cicloJogador2.getPontuacao()) * -1;
					if (comparacao == 0) {
						return cicloJogador1.getJogador().getNome().compareTo(cicloJogador2.getJogador().getNome());
					}
					return comparacao;
				}
				return 1;
			}
		});
		int posRanking = 1;
		int acumulado = 0;
		for (int i = 0; i < ranking.size(); i++) {
			ranking.get(i).setRanking(posRanking);
			if (i + 1 < ranking.size() && ranking.get(i).getPontuacao().equals(ranking.get(i + 1).getPontuacao())) {
				acumulado++;
			} else {
				posRanking += acumulado;
				posRanking++;
				acumulado = 0;
			}
		}
	}

	public boolean isPrimeiroJogadorAcima(Jogador primeiroJogador, Jogador segundoJogador) {
		Map<Jogador, CicloJogador> map = getMapaDeJogadoresERanking();
		if (map.get(primeiroJogador).getRanking() < map.get(segundoJogador).getRanking()) {
			return true;
		}
		return false;
	}

	private Map<Jogador, CicloJogador> getMapaDeJogadoresERanking() {
		PersistenceHelper.initialize("ranking", this);
		Map<Jogador, CicloJogador> map = new HashMap<Jogador, CicloJogador>();
		for (CicloJogador cicloJogador : ranking) {
			map.put(cicloJogador.getJogador(), cicloJogador);
		}
		return map;
	}

	public Integer getIndexNaLista(Jogador jogador) {
		for (int i = 0; i < ranking.size(); i++) {
			if (ranking.get(i).getJogador().equals(jogador)) {
				return i;
			}
		}
		return null;
	}

	public List<Jogador> getJogadoresDoRanking() {
		List<Jogador> jogadoresDoRanking = new ArrayList<Jogador>();
		for (CicloJogador cicloJogador : ranking) {
			jogadoresDoRanking.add(cicloJogador.getJogador());
		}
		return jogadoresDoRanking;
	}

	public List<Jogador> getJogadoresHabilitadosDoRanking() {
		List<Jogador> jogadoresDoRanking = new ArrayList<Jogador>();
		for (CicloJogador cicloJogador : ranking) {
			if (cicloJogador.getHabilitado()) {
				jogadoresDoRanking.add(cicloJogador.getJogador());
			}
		}
		return jogadoresDoRanking;
	}

	public boolean existeJogoComJogador(Jogador jogador) {
		for (Rodada rodada : getRodadas()) {
			if (rodada.getJogadoresDosJogos().contains(jogador)) {
				return true;
			}
		}
		return false;
	}

	public int getPontuacaoExcedente(Integer numero, Jogador jogador) {
		if (getParametros().getRodadasDeHistoricoMantidasParaCalculoDoRanking() != null
				&& numero > getParametros().getRodadasDeHistoricoMantidasParaCalculoDoRanking()) {
			int indiceRodadaExcedente = numero - getParametros().getRodadasDeHistoricoMantidasParaCalculoDoRanking()
					- 1;
			return getRodadas().get(indiceRodadaExcedente).getJogadorJogoBarragem(jogador).getPontuacaoObtida();
		}

		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barragem.getId() == null) ? 0 : barragem.getId().hashCode());
		result = prime * result + ((dataInicio == null) ? 0 : dataInicio.hashCode());
		result = prime * result + ((getNome() == null) ? 0 : getNome().hashCode());
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
		Ciclo other = (Ciclo) obj;
		if (barragem.getId() == null) {
			if (other.barragem.getId() != null)
				return false;
		} else if (!barragem.getId().equals(other.barragem.getId()))
			return false;
		if (dataInicio == null) {
			if (other.dataInicio != null)
				return false;
		} else if (!dataInicio.equals(other.dataInicio))
			return false;
		if (getNome() == null) {
			if (other.getNome() != null)
				return false;
		} else if (!getNome().equals(other.getNome()))
			return false;
		return true;
	}
}