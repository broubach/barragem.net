package net.barragem.persistence.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
public class Evento extends BaseEntity {

	private Date data;

	@OneToMany(mappedBy = "evento")
	protected List<JogadorEvento> jogadoresEventos;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<JogadorEvento> getJogadoresEventos() {
		return jogadoresEventos;
	}

	public void setJogadoresEventos(List<JogadorEvento> jogadoresEventos) {
		this.jogadoresEventos = jogadoresEventos;
	}
}
