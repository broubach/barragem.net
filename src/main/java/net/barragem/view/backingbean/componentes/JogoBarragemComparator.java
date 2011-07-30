package net.barragem.view.backingbean.componentes;

import java.util.Comparator;

import net.barragem.persistence.entity.JogoBarragem;

public class JogoBarragemComparator implements Comparator<JogoBarragem> {
	public int compare(JogoBarragem o1, JogoBarragem o2) {
		if (o1.getId() != null && o2.getId() != null) {
			return o1.getId().compareTo(o2.getId());
		} else if (o1.getData() != null && o2.getData() != null) {
			return o2.getData().compareTo(o1.getData());
		}
		return -1;
	}
}