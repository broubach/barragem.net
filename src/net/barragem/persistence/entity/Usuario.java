package net.barragem.persistence.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "loginQuery", query = "from Usuario usuario where usuario.email = :email and usuario.senha = :senha")
@Table(name = "usuario")
public class Usuario extends BaseEntity {

	private String nome;
	private String sobrenome;
	private String email;
	private String senha;
	private SexoEnum sexo;
	private Date aniversario;
	private Date dataUltimoAcesso;

	@OneToOne(mappedBy = "usuarioCorrespondente")
	private Jogador jogador;

	@OneToOne(mappedBy = "usuario")
	private DetalhePerfil detalhePerfil;

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

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String login) {
		this.email = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public List<Barragem> getBarragens() {
		return barragens;
	}

	public void setBarragens(List<Barragem> barragens) {
		this.barragens = barragens;
	}

	public Date getAniversario() {
		return aniversario;
	}

	public void setAniversario(Date aniversario) {
		this.aniversario = aniversario;
	}

	public DetalhePerfil getDetalhePerfil() {
		return detalhePerfil;
	}

	public void setDetalhePerfil(DetalhePerfil detalhePerfil) {
		this.detalhePerfil = detalhePerfil;
	}

	public SexoEnum getSexo() {
		return sexo;
	}

	public void setSexo(SexoEnum sexo) {
		this.sexo = sexo;
	}

	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}
}