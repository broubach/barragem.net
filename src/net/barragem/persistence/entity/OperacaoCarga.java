package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "operacaocarga")
public class OperacaoCarga extends Operacao {

	private Double valorMonetario;

	public Double getValorMonetario() {
		return valorMonetario;
	}

	public void setValorMonetario(Double valor) {
		this.valorMonetario = valor;
	}
}
