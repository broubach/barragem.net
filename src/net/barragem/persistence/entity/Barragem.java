package net.barragem.persistence.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "barragemPorUsuarioQuery", query = "select distinct barragem from Barragem barragem join fetch barragem.jogadores where barragem.administrador = :usuario")
public class Barragem extends BaseEntity {

	private String nome;
	private String local;

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario administrador;

	@ManyToMany
	@JoinTable(inverseJoinColumns = @JoinColumn(name = "jogador_id", referencedColumnName = "id"))
	private List<Jogador> jogadores;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public Usuario getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Usuario administrador) {
		this.administrador = administrador;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}
}