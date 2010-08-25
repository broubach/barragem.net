package net.barragem.view.backingbean.componentes;

import java.util.Comparator;

import net.barragem.persistence.entity.Evento;

public class EventoMaisRecenteComparator implements Comparator<Evento> {
	public int compare(Evento o1, Evento o2) {
		if (o1.getData() != null && o2.getData() != null && o1.getData().equals(o2.getData())) {
			if (o1.getHora() != null && o2.getHora() != null) {
				return o1.getHora().compareTo(o2.getHora());
			} else if (o1.getHora() == null) {
				return 1;
			} else if (o2.getHora() == null) {
				return -1;
			}
			return 0;
		} else if (o1.getData() != null && o2.getData() != null && !o1.getData().equals(o2.getData())) {
			return o1.getData().compareTo(o2.getData());
		} else if (o1.getData() == null) {
			return 1;
		} else if (o2.getData() == null) {
			return -1;
		}
		return 0;
	}
}