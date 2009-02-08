package net.barragem.persistence.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Date data;

	@OneToMany(mappedBy = "evento")
	protected List<JogadorEvento> jogadoresEventos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
