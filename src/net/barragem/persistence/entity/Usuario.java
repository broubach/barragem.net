package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NamedQueries( {
		@NamedQuery(name = "loginQuery", query = "select u from Usuario u left outer join u.perfil p where u.email = :email and u.senha = :senha"),
		@NamedQuery(name = "perfilQuery", query = "select u from Usuario u left outer join u.perfil p where u.id = :id"),
		@NamedQuery(name = "barragensDeUsuarioQuery", query = "select distinct c.barragem from Ciclo c join c.ranking r join r.jogador j join j.usuarioCorrespondente uc where uc.id = :id"),
		@NamedQuery(name = "pesquisaUsuarioQuery", query = "select u.jogador from Usuario u left outer join u.perfil p where upper(u.nome) like :p or upper(u.sobrenome) like :p or upper(p.clubeMaisFrequentadoNome) like :p or upper(p.clubeMaisFrequentadoCidade) like :p or upper(p.professorNome) like :p or upper(p.raquete) like :p"),
		@NamedQuery(name = "emailExistenteQuery", query = "select 1 from Usuario usuario where usuario.email = :email") })
@Table(name = "usuario")
public class Usuario extends BaseEntity {

	private String nome;
	private String sobrenome;
	private String email;
	private String senha;
	private SexoEnum sexo;
	private Date aniversario;
	private Date dataUltimoAcesso;
	private Date dataPenultimoAcesso;

	@OneToOne
	private Jogador jogador;

	@OneToOne(mappedBy = "usuario")
	private Perfil perfil;

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

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
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

	public Date getDataPenultimoAcesso() {
		return dataPenultimoAcesso;
	}

	public void setDataPenultimoAcesso(Date dataPenultimoAcesso) {
		this.dataPenultimoAcesso = dataPenultimoAcesso;
	}

	public String getNomeCompletoUpper() {
		return new StringBuilder().append(nome).append(" ").append(sobrenome).toString().toUpperCase();
	}

	public String getNomeCompletoCapital() {
		StringBuilder strBuilder = new StringBuilder();
		String nomeCompleto = getNomeCompletoUpper();
		String[] words = nomeCompleto.split(" ");
		for (String s : words) {
			strBuilder.append(capitalize(s)).append(" ");
		}
		return strBuilder.toString();
	}

	public static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	public boolean hasJogador(Usuario usuario) {
		for (Jogador jogador : getJogadores()) {
			if (jogador.getUsuarioCorrespondente() != null
					&& jogador.getUsuarioCorrespondente().getId().equals(usuario.getId())) {
				return true;
			}
		}
		return false;
	}

	public List<Jogador> getJogadoresSemOUsuarioCorrespondente() {
		List<Jogador> result = new ArrayList<Jogador>();
		result.addAll(getJogadores());
		Jogador jogador = null;
		for (Iterator<Jogador> it = result.iterator(); it.hasNext();) {
			jogador = it.next();
			if (jogador.getUsuarioCorrespondente() != null
					&& jogador.getUsuarioCorrespondente().getId().equals(getId())) {
				it.remove();
			}
		}
		return result;
	}
}