package net.barragem.view.backingbean.componentes;

import java.util.Comparator;

import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.JogadorJogo;

public class JogadorEventoComparatorVencedorPrimeiro implements Comparator<JogadorEvento> {
	public int compare(JogadorEvento o1, JogadorEvento o2) {
		if (Boolean.TRUE.equals(((JogadorJogo) o1).getVencedor())) {
			return -1;
		} else if (Boolean.TRUE.equals(((JogadorJogo) o2).getVencedor())) {
			return 1;
		} else if (o1.getJogador() != null && o2.getJogador() != null) {
			return o1.getJogador().getNome().compareTo(o2.getJogador().getNome());
		}
		return -1;
	}
}
