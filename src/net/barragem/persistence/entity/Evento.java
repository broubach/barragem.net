package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@NamedQuery(name = "meusEventosQuery", query = "select distinct e from Evento e join e.jogadoresEventos je where je.jogador.usuarioCorrespondente = :usuario order by e.data desc")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "evento")
public abstract class Evento extends BaseEntity implements Cloneable {

	@ValidateRequired
	private Date data;

	@ValidateRequired
	private Date hora;

	@OneToMany(mappedBy = "evento", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private List<JogadorEvento> jogadoresEventos;

	@Transient
	private Usuario usuarioLogado;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public List<JogadorEvento> getJogadoresEventos() {
		if (jogadoresEventos == null) {
			jogadoresEventos = new ArrayList<JogadorEvento>();
		}
		return jogadoresEventos;
	}

	public void setJogadoresEventos(List<JogadorEvento> jogadoresEventos) {
		this.jogadoresEventos = jogadoresEventos;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
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
		((Evento) newObject).setId(getId());
		((Evento) newObject).setData(data);
		((Evento) newObject).setHora(hora);

		List<JogadorEvento> clonedJogadoresEventos = new ArrayList<JogadorEvento>();
		JogadorEvento clonedJogadorEvento = null;
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			clonedJogadorEvento = (JogadorEvento) ((JogadorEvento) jogadorEvento).clone();
			clonedJogadorEvento.setEvento((Evento) newObject);
			clonedJogadoresEventos.add(clonedJogadorEvento);
		}
		((Evento) newObject).setJogadoresEventos(clonedJogadoresEventos);
	}

	public abstract String getTipoStr();

	public abstract String getJogadoresStr();

	public String getComentarioUsuarioLogado() {
		return getJogadorEventoUsuarioLogado().getComentario();
	}

	public JogadorEvento getJogadorEventoUsuarioLogado() {
		if (usuarioLogado == null) {
			throw new IllegalStateException();
		}
		for (JogadorEvento jogadorEvento : jogadoresEventos) {
			if (jogadorEvento.getJogador().getUsuarioCorrespondente() != null
					&& usuarioLogado.getId().equals(jogadorEvento.getJogador().getUsuarioCorrespondente().getId())) {
				return jogadorEvento;
			}
		}
		return null;
	}
}