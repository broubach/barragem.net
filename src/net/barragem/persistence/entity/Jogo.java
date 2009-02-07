package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id")
public class Jogo extends Evento {

	@OneToOne
	private Placar placar;
	private String tipo;

	public Placar getPlacar() {
		return placar;
	}

	public void setPlacar(Placar placar) {
		this.placar = placar;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}