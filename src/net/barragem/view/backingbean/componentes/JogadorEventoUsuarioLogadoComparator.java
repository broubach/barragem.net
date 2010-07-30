package net.barragem.view.backingbean.componentes;

import java.util.Comparator;

import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.Usuario;

public class JogadorEventoUsuarioLogadoComparator implements Comparator<JogadorEvento> {
	private Usuario usuarioLogado;

	public JogadorEventoUsuarioLogadoComparator(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public int compare(JogadorEvento o1, JogadorEvento o2) {
		if (((JogadorEvento) o1).getJogador().getUsuarioCorrespondente() != null
				&& ((JogadorEvento) o1).getJogador().getUsuarioCorrespondente().getId().equals(usuarioLogado.getId())) {
			return -1;
		} else if (((JogadorEvento) o2).getJogador().getUsuarioCorrespondente() != null
				&& ((JogadorEvento) o2).getJogador().getUsuarioCorrespondente().getId().equals(usuarioLogado.getId())) {
			return 1;
		}
		return 0;
	}
}
