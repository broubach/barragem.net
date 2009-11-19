package net.barragem.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detalheperfil")
public class DetalhePerfil extends BaseEntity {

	private String clubeMaisFrequentadoNome;
	private String clubeMaisFrequentadoCidade;
	private Boolean professor;
	private String professorNome;
	private Date dataInicioPratica;
	private LadoForehandEnum ladoForhand;
	private TipoBackhandEnum tipoBackhand;
	private String resumo;

	@OneToOne
	private Usuario usuario;

	public String getClubeMaisFrequentadoNome() {
		return clubeMaisFrequentadoNome;
	}

	public void setClubeMaisFrequentadoNome(String clubeMaisFrequentadoNome) {
		this.clubeMaisFrequentadoNome = clubeMaisFrequentadoNome;
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

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public LadoForehandEnum getLadoForhand() {
		return ladoForhand;
	}

	public void setLadoForhand(LadoForehandEnum ladoForhand) {
		this.ladoForhand = ladoForhand;
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

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}