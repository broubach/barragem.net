package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "parcial")
public class Parcial extends BaseEntity implements Cloneable {
	private Integer parcialVencedor;
	private Integer parcialPerdedor;

	@ManyToOne
	private Placar placar;

	public Integer getParcialVencedor() {
		return parcialVencedor;
	}

	public void setParcialVencedor(Integer parcialVencedor) {
		this.parcialVencedor = parcialVencedor;
	}

	public Integer getParcialPerdedor() {
		return parcialPerdedor;
	}

	public void setParcialPerdedor(Integer parcialPerdedor) {
		this.parcialPerdedor = parcialPerdedor;
	}

	public Placar getPlacar() {
		return placar;
	}

	public void setPlacar(Placar placar) {
		this.placar = placar;
	}

	public Object clone() {
		Parcial cloned = new Parcial();
		cloned.setId(getId());
		cloned.setParcialPerdedor(parcialPerdedor);
		cloned.setParcialVencedor(parcialVencedor);
		cloned.setPlacar(placar);
		return cloned;
	}
}