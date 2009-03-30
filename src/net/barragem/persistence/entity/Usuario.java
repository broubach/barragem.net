package net.barragem.persistence.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@NamedQuery(name = "loginQuery", query = "from Usuario usuario where usuario.login = :login and usuario.senha = :senha")
public class Usuario extends BaseEntity {

	private String nome;
	private String login;
	private String senha;

	@OneToOne(mappedBy = "usuarioCorrespondente")
	private Jogador jogador;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioDono")
	private List<Jogador> jogadores;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "administrador")
	private List<Barragem> barragens;

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

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Barragem> getBarragens() {
		return barragens;
	}

	public void setBarragens(List<Barragem> barragens) {
		this.barragens = barragens;
	}
}