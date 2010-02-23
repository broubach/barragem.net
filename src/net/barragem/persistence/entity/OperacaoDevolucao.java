package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "operacaodevolucao")
public class OperacaoDevolucao extends Operacao {

	private Boolean automatica;
	private String motivoDevolucao;

	public String getMotivoDevolucao() {
		return motivoDevolucao;
	}

	public void setMotivoDevolucao(String motivoDevolucao) {
		this.motivoDevolucao = motivoDevolucao;
	}

	public Boolean getAutomatica() {
		return automatica;
	}

	public void setAutomatica(Boolean automatica) {
		this.automatica = automatica;
	}
}
