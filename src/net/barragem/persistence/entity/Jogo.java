package net.barragem.persistence.entity;

import java.util.Iterator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "jogo")
public class Jogo extends Evento {

	@OneToOne(cascade = CascadeType.ALL)
	private Placar placar;
	private SimplesDuplasEnum tipo;

	public Placar getPlacar() {
		return placar;
	}

	public void setPlacar(Placar placar) {
		this.placar = placar;
	}

	public SimplesDuplasEnum getTipo() {
		return tipo;
	}

	public void setTipo(SimplesDuplasEnum tipo) {
		this.tipo = tipo;
	}

	public Jogador getJogadorVencedorSimples() {
		return getJogadorBarragemVencedorSimples() != null ? getJogadorBarragemVencedorSimples().getJogador() : null;
	}

	public Jogador getJogadorPerdedorSimples() {
		return getJogadorBarragemPerdedorSimples() != null ? getJogadorBarragemPerdedorSimples().getJogador() : null;
	}

	public void inverteParciaisVencedorasEPerdedoras() {
		Parcial parcial = null;
		int aux = 0;
		for (Iterator<Parcial> it = getPlacar().getParciais().iterator(); it.hasNext();) {
			parcial = it.next();
			aux = parcial.getParcialPerdedor();
			parcial.setParcialPerdedor(parcial.getParcialVencedor());
			parcial.setParcialVencedor(aux);
		}
	}

	public JogadorJogoBarragem getJogadorBarragemVencedorSimples() {
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (((JogadorJogo) jogadorEvento).getVencedor()) {
				return (JogadorJogoBarragem) jogadorEvento;
			}
		}
		return null;
	}

	public JogadorJogoBarragem getJogadorBarragemPerdedorSimples() {
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (!((JogadorJogo) jogadorEvento).getVencedor()) {
				return (JogadorJogoBarragem) jogadorEvento;
			}
		}
		return null;
	}
}