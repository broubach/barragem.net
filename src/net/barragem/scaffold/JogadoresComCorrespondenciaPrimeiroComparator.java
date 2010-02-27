package net.barragem.scaffold;

import java.util.Comparator;

import net.barragem.persistence.entity.Jogador;

public class JogadoresComCorrespondenciaPrimeiroComparator implements Comparator<Jogador> {

	@Override
	public int compare(Jogador o1, Jogador o2) {
		if (o1.getUsuarioCorrespondente() == null) {
			if (o2.getUsuarioCorrespondente() == null) {
				return o1.getNome().compareTo(o2.getNome());
			}
			return 1;
		}
		if (o2.getUsuarioCorrespondente() == null) {
			return -1;
		}
		return o1.getNome().compareTo(o2.getNome());
	}

}
