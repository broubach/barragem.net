package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Set {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer parcialVencedor;
	private Integer parcialPerdedor;

	@OneToMany
	@JoinTable(name = "set_vencedor", inverseJoinColumns = @JoinColumn(name = "jogador_id", referencedColumnName = "id"))
	private java.util.Set<Jogador> vencedores;

	@ManyToMany
	@JoinTable(name = "set_perdedor", inverseJoinColumns = @JoinColumn(name = "jogador_id", referencedColumnName = "id"))
	private java.util.Set<Jogador> perdedores;

	@ManyToOne
	private Placar placar;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParcialVencedor() {
		return parcialVencedor;
	}

	public void setParcialVencedor(Integer parcialVencedor) {
		this.parcialVencedor = parcialVencedor;
	}

	public Integer getParcialPerdedor() {
		return parcialPerdedor;
	}

	public void setParcialPerdedor(Integer parcialPerdedor) {
		this.parcialPerdedor = parcialPerdedor;
	}

	public java.util.Set<Jogador> getVencedores() {
		return vencedores;
	}

	public void setVencedores(java.util.Set<Jogador> vencedores) {
		this.vencedores = vencedores;
	}

	public java.util.Set<Jogador> getPerdedores() {
		return perdedores;
	}

	public void setPerdedores(java.util.Set<Jogador> perdedores) {
		this.perdedores = perdedores;
	}

	public Placar getPlacar() {
		return placar;
	}

	public void setPlacar(Placar placar) {
		this.placar = placar;
	}
}