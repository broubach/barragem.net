package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "jogadorevento")
public class JogadorEvento extends BaseEntity implements Cloneable {
	@ManyToOne
	private Evento evento;

	@ManyToOne
	private Jogador jogador;

	private String comentario;

	public JogadorEvento() {
	}

	public JogadorEvento(Evento evento) {
		this.evento = evento;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((evento.getId() == null) ? 0 : evento.getId().hashCode());
		result = prime * result + ((jogador.getId() == null) ? 0 : jogador.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JogadorEvento other = (JogadorEvento) obj;
		if (evento.getId() == null) {
			if (other.getEvento().getId() != null)
				return false;
		} else if (!evento.getId().equals(other.getEvento().getId()))
			return false;
		if (jogador.getId() == null) {
			if (other.getJogador().getId() != null)
				return false;
		} else if (!jogador.getId().equals(other.getJogador().getId()))
			return false;
		return true;
	}

	public Object clone() {
		try {
			Object newObject = getClass().newInstance();
			cloneTo(newObject);
			return newObject;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void cloneTo(Object newObject) {
		((JogadorEvento) newObject).setId(getId());
		((JogadorEvento) newObject).setEvento(evento);
		((JogadorEvento) newObject).setJogador(jogador);
		((JogadorEvento) newObject).setComentario(comentario);
	}

}
