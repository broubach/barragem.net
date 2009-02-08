package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Jogador extends BaseEntity {
	private String nome;

	@OneToOne
	private Usuario usuarioCorrespondente;

	@ManyToOne
	private Usuario usuarioDono;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getUsuarioCorrespondente() {
		return usuarioCorrespondente;
	}

	public void setUsuarioCorrespondente(Usuario usuario) {
		this.usuarioCorrespondente = usuario;
	}

	public Usuario getUsuarioDono() {
		return usuarioDono;
	}

	public void setUsuarioDono(Usuario usuarioDono) {
		this.usuarioDono = usuarioDono;
	}
}