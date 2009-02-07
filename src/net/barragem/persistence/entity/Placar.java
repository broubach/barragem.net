package net.barragem.persistence.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Placar {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(mappedBy = "placar")
	private Set<net.barragem.persistence.entity.Set> sets;

	@ManyToMany
	@JoinTable(name = "placar_vencedor", inverseJoinColumns = @JoinColumn(name = "jogador_id", referencedColumnName = "id"))
	private Set<Jogador> vencedores;

	@ManyToMany
	@JoinTable(name = "placar_perdedor", inverseJoinColumns = @JoinColumn(name = "jogador_id", referencedColumnName = "id"))
	private Set<Jogador> perdedores;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<net.barragem.persistence.entity.Set> getSets() {
		return sets;
	}

	public void setSets(Set<net.barragem.persistence.entity.Set> sets) {
		this.sets = sets;
	}

	public Set<Jogador> getVencedores() {
		return vencedores;
	}

	public void setVencedores(Set<Jogador> vencedores) {
		this.vencedores = vencedores;
	}

	public Set<Jogador> getPerdedores() {
		return perdedores;
	}

	public void setPerdedores(Set<Jogador> perdedores) {
		this.perdedores = perdedores;
	}
}
