package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "jogadorjogo")
public class JogadorJogo extends JogadorEvento {

	private Boolean vencedor;

	public Boolean getVencedor() {
		return vencedor;
	}

	public void setVencedor(Boolean vencedor) {
		this.vencedor = vencedor;
	}

	@Override
	public void cloneTo(Object newObject) {
		super.cloneTo(newObject);
		((JogadorJogo) newObject).setVencedor(vencedor);
	}

}
