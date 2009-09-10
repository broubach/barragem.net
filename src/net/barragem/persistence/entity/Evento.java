package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "evento")
public class Evento extends BaseEntity {

	private Date data;

	@OneToMany(mappedBy = "evento", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private List<JogadorEvento> jogadoresEventos;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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
}
