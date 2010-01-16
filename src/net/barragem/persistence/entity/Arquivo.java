package net.barragem.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "arquivo")
public class Arquivo extends BaseEntity {

	@Lob
	private byte[] dado;
	private String nome;
	private Integer tamanho;
	private Date data;
	private String mime;

	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario dono;

	public byte[] getDado() {
		return dado;
	}

	public void setDado(byte[] dado) {
		this.dado = dado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Usuario getDono() {
		return dono;
	}

	public void setDono(Usuario dono) {
		this.dono = dono;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}
}