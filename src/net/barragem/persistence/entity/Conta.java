package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "conta")
@NamedQuery(name = "findContaPorUsuarioQuery", query = "from Conta c where c.proprietario = :proprietario")
public class Conta extends BaseEntity {

	@OneToOne
	private Usuario proprietario;
	private Integer saldo = new Integer(0);

	public Usuario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Usuario proprietario) {
		this.proprietario = proprietario;
	}

	public Integer getSaldo() {
		return saldo;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}

	public OperacaoDebitoJogoBarragem criaOperacaoDebitoJogoBarragem(int quantidade) {
		saldo -= quantidade;
		OperacaoDebitoJogoBarragem operacao = new OperacaoDebitoJogoBarragem();
		operacao.setQuantidade(quantidade);
		operacao.setConta(this);
		return operacao;
	}

	public OperacaoDevolucao criaOperacaoDevolucao(int quantidade) {
		saldo += quantidade;
		OperacaoDevolucao operacao = new OperacaoDevolucao();
		operacao.setAutomatica(true);
		operacao.setMotivoDevolucao("exclusão de jogo");
		operacao.setQuantidade(quantidade);
		operacao.setConta(this);
		return operacao;
	}
}