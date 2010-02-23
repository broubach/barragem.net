package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "conta")
public class Conta extends BaseEntity {

	private Usuario proprietario;
	private Integer saldo;

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
}