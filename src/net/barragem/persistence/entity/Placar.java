package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;

@Entity
@Table(name = "placar")
public class Placar extends BaseEntity implements Cloneable {

	@OneToMany(mappedBy = "placar", cascade = CascadeType.ALL)
	private List<Parcial> parciais;
	private Boolean wo = Boolean.FALSE;

	public List<Parcial> getParciais() {
		return parciais;
	}

	public void setParciais(List<Parcial> parciais) {
		this.parciais = parciais;
	}

	public Boolean getWo() {
		return wo;
	}

	public void setWo(Boolean wo) {
		this.wo = wo;
	}

	public String toString() {
		if (!Hibernate.isInitialized(parciais)) {
			return super.toString();
		}
		if (wo.equals(Boolean.TRUE)) {
			return "WO";
		}
		StringBuilder result = new StringBuilder();
		for (Parcial parcial : parciais) {
			if (parcial.getParcialVencedor() == null || parcial.getParcialPerdedor() == null) {
				continue;
			}
			result.append(parcial.getParcialVencedor());
			result.append("/");
			result.append(parcial.getParcialPerdedor());
			result.append(" ");
		}
		return result.toString().trim();
	}

	public boolean isVencedorPerdeuAlgumaParcial() {
		for (Parcial parcial : parciais) {
			if (parcial.getParcialPerdedor() > parcial.getParcialVencedor()) {
				return true;
			}
		}
		return false;
	}

	public Object clone() {
		Placar cloned = new Placar();
		cloned.setWo(getWo());

		Parcial clonedParcial = null;
		List<Parcial> clonedParciais = new ArrayList<Parcial>();
		for (Parcial parcial : getParciais()) {
			clonedParcial = (Parcial) parcial.clone();
			clonedParcial.setPlacar(cloned);
			clonedParciais.add(clonedParcial);
		}
		cloned.setParciais(clonedParciais);
		return cloned;
	}
}