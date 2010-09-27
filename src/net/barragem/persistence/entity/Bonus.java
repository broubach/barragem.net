package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import net.barragem.scaffold.ValidatableSampleImpl;

@Entity
@Table(name = "bonus")
@NamedQueries( { @NamedQuery(name = "bonusQuery", query = "select distinct j.nome, bo.justificativa, bo.valor from Bonus bo join bo.jogador j join bo.rodada r join r.ciclo c join c.barragem b where r.numero = :numRodada and b.id = :barragemId") })
public class Bonus extends BaseEntity implements Validatable {

	@ManyToOne
	private Rodada rodada;
	@ValidateRequired
	private String justificativa;
	@ValidateRequired
	private Integer valor;

	@ValidateRequired
	@ManyToOne
	private Jogador jogador;

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Rodada getRodada() {
		return rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
	}

	@Override
	public List<String> validate() {
		List<String> result = new ArrayList<String>();
		result.addAll(new ValidatableSampleImpl(this).validate());
		return result;
	}
}
