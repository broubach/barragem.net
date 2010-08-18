package net.barragem.scaffold;

import java.util.Comparator;

import net.barragem.persistence.entity.Jogador;

public class JogadoresMaisRelevantesPrimeiroComparator implements Comparator<Jogador> {
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
		if (o1.getUsuarioCorrespondente().getPerfil() == null) {
			if (o2.getUsuarioCorrespondente().getPerfil() == null) {
				return o2.getUsuarioCorrespondente().getDataUltimoAcesso().compareTo(
						o1.getUsuarioCorrespondente().getDataUltimoAcesso());
			}
			return 1;
		}
		if (o2.getUsuarioCorrespondente().getPerfil() == null) {
			return -1;
		}
		if (o1.getUsuarioCorrespondente().getPerfil().getHash() == null) {
			if (o2.getUsuarioCorrespondente().getPerfil().getHash() == null) {
				return o2.getUsuarioCorrespondente().getDataUltimoAcesso().compareTo(
						o1.getUsuarioCorrespondente().getDataUltimoAcesso());
			}
			return 1;
		}
		if (o2.getUsuarioCorrespondente().getPerfil().getHash() == null) {
			return -1;
		}
		if (o1.getUsuarioCorrespondente().getPerfil().getFoto().getTamanho().equals(15956)) {
			return 1;
		}
		if (o2.getUsuarioCorrespondente().getPerfil().getFoto().getTamanho().equals(15956)) {
			return -1;
		}
		return o2.getUsuarioCorrespondente().getDataUltimoAcesso().compareTo(
				o1.getUsuarioCorrespondente().getDataUltimoAcesso());
	}

}
