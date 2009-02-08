package net.barragem.persistence.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Placar extends BaseEntity {

	@OneToMany(mappedBy = "placar")
	private List<net.barragem.persistence.entity.Set> sets;

	@ManyToMany
	@JoinTable(name = "placar_vencedor", inverseJoinColumns = @JoinColumn(name = "jogador_id", referencedColumnName = "id"))
	private List<Jogador> vencedores;

	@ManyToMany
	@JoinTable(name = "placar_perdedor", inverseJoinColumns = @JoinColumn(name = "jogador_id", referencedColumnName = "id"))
	private List<Jogador> perdedores;

	public List<net.barragem.persistence.entity.Set> getSets() {
		return sets;
	}

	public void setSets(List<net.barragem.persistence.entity.Set> sets) {
		this.sets = sets;
	}

	public List<Jogador> getVencedores() {
		return vencedores;
	}

	public void setVencedores(List<Jogador> vencedores) {
		this.vencedores = vencedores;
	}

	public List<Jogador> getPerdedores() {
		return perdedores;
	}

	public void setPerdedores(List<Jogador> perdedores) {
		this.perdedores = perdedores;
	}
}