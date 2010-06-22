package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;

@Entity
@NamedQuery(name = "placarFetchParcialQuery", query = "select p from Placar p join fetch p.parciais where p.id in(:idsPlacar)")
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
			if (parcial.getParcialPerdedor() != null && parcial.getParcialVencedor() != null
					&& (parcial.getParcialPerdedor() > parcial.getParcialVencedor())) {
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

	public void completaSetsSeNecessario(Integer numeroDeSets) {
		for (int i = 0; i < numeroDeSets; i++) {
			if (i >= parciais.size()) {
				Parcial parcial = new Parcial();
				parcial.setPlacar(this);
				parciais.add(parcial);
			}
		}
	}

	public List<Parcial> removeSetsIncompletos() {
		Parcial parcial = null;
		List<Parcial> parciaisRemovidas = new ArrayList<Parcial>();
		for (Iterator<Parcial> it = getParciais().iterator(); it.hasNext();) {
			parcial = it.next();
			if (parcial.getParcialVencedor() == null
					|| parcial.getParcialPerdedor() == null
					|| getWo()
					|| (parcial.getParcialVencedor().equals(new Integer(0)) && parcial.getParcialPerdedor().equals(
							new Integer(0)))) {
				parciaisRemovidas.add(parcial);
				parcial.setPlacar(null);
				it.remove();
			}
		}
		return parciaisRemovidas;
	}
}