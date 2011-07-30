package net.barragem.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.barragem.exception.SaldoInsuficienteException;

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

    public OperacaoDebitoJogoBarragem criaOperacaoDebitoJogoBarragem(int quantidade) throws SaldoInsuficienteException {
        if (saldo >= quantidade) {
            saldo -= quantidade;
            OperacaoDebitoJogoBarragem operacao = new OperacaoDebitoJogoBarragem();
            operacao.setQuantidade(quantidade);
            operacao.setConta(this);
            operacao.setData(new Date());
            return operacao;
        }
        throw new SaldoInsuficienteException();
    }

    public OperacaoDevolucao criaOperacaoDevolucao(int quantidade) {
        saldo += quantidade;
        OperacaoDevolucao operacao = new OperacaoDevolucao();
        operacao.setAutomatica(true);
        operacao.setMotivoDevolucao("exclusão de jogo");
        operacao.setQuantidade(quantidade);
        operacao.setConta(this);
        operacao.setData(new Date());
        return operacao;
    }

    public OperacaoCarga criaOperacaoCarga(int quantidade, double precoUnitario) {
        saldo += quantidade;
        OperacaoCarga operacao = new OperacaoCarga();
        operacao.setQuantidade(quantidade);
        operacao.setConta(this);
        operacao.setData(new Date());
        operacao.setValorMonetario(precoUnitario * quantidade);
        return operacao;
    }

    public boolean possuiSaldoSuficiente(int quantidade) {
        if (saldo >= quantidade) {
            return true;
        }
        return false;
    }
}