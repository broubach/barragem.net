package net.barragem.persistence.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "perfil")
public class Perfil extends BaseEntity {

	private String clubeMaisFrequentadoNome;
	private String clubeMaisFrequentadoCidade;
	private Boolean professor;
	private String professorNome;
	private Date dataInicioPratica;
	private LadoForehandEnum ladoForehand;
	private TipoBackhandEnum tipoBackhand;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Arquivo foto;

	@OneToOne
	private Usuario usuario;

	public Perfil() {
	}

	public Perfil(Arquivo foto) {
		this.foto = foto;
	}

	public String getClubeMaisFrequentadoNome() {
		return clubeMaisFrequentadoNome;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Arquivo getFoto() {
		return foto;
	}

	public void setFoto(Arquivo foto) {
		this.foto = foto;
	}

	public String getClubeMaisFrequentadoCidade() {
		return clubeMaisFrequentadoCidade;
	}

	public void setClubeMaisFrequentadoCidade(String clubeMaisFrequentadoCidade) {
		this.clubeMaisFrequentadoCidade = clubeMaisFrequentadoCidade;
	}

	public Boolean getProfessor() {
		return professor;
	}

	public void setProfessor(Boolean professor) {
		this.professor = professor;
	}

	public String getProfessorNome() {
		return professorNome;
	}

	public void setProfessorNome(String professorNome) {
		this.professorNome = professorNome;
	}

	public Date getDataInicioPratica() {
		return dataInicioPratica;
	}

	public void setDataInicioPratica(Date dataInicioPratica) {
		this.dataInicioPratica = dataInicioPratica;
	}

	public LadoForehandEnum getLadoForehand() {
		return ladoForehand;
	}

	public void setLadoForehand(LadoForehandEnum ladoForehand) {
		this.ladoForehand = ladoForehand;
	}

	public TipoBackhandEnum getTipoBackhand() {
		return tipoBackhand;
	}

	public void setTipoBackhand(TipoBackhandEnum tipoBackhand) {
		this.tipoBackhand = tipoBackhand;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setClubeMaisFrequentadoNome(String clubeMaisFrequentadoNome) {
		this.clubeMaisFrequentadoNome = clubeMaisFrequentadoNome;
	}
}
