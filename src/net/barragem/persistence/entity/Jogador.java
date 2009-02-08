package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Jogador {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@OneToOne
	private Usuario usuarioCorrespondente;

	@ManyToOne
	private Usuario usuarioDono;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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