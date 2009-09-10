package net.barragem.view.dto;

import net.barragem.persistence.entity.Jogador;

public class JogadorSelecionavelDto {
	private Jogador jogador;
	private boolean selecionado;

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}
}
