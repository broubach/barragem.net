package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.barragem.scaffold.ReflectionHelper;

@Entity
@NamedQueries( {
		@NamedQuery(name = "totalJogadoresDeUsuarioQuery", query = "select count(*) from Jogador where usuarioDono = :usuario"),
		@NamedQuery(name = "jogadoresPorUsuarioDonoQuery", query = "from Jogador where usuarioDono.id=:idUsuarioDono"),
		@NamedQuery(name = "pesquisaJogadorQuery", query = "select u.jogador from Usuario u left outer join u.perfil p where upper(u.nome) like :p or upper(u.sobrenome) like :p or upper(p.clubeMaisFrequentadoNome) like :p or upper(p.clubeMaisFrequentadoCidade) like :p or upper(p.professorNome) like :p or upper(p.raquete) like :p"),
		@NamedQuery(name = "pesquisaJogadorForaDaListaQuery", query = "select u.jogador from Usuario u left outer join u.perfil p where (upper(u.nome) like :p or upper(u.sobrenome) like :p or upper(p.clubeMaisFrequentadoNome) like :p or upper(p.clubeMaisFrequentadoCidade) like :p or upper(p.professorNome) like :p or upper(p.raquete) like :p) and u <> :u and u not in (select uc from Jogador j join j.usuarioCorrespondente uc where j.usuarioDono = :u)"),
		@NamedQuery(name = "pesquisaJogadorDeUsuarioQuery", query = "select j from Usuario u join u.jogadores j left outer join j.usuarioCorrespondente uc left outer join uc.perfil p where (upper(uc.nome) like :p1 or upper(uc.sobrenome) like :p1 or upper(p.clubeMaisFrequentadoNome) like :p1 or upper(p.clubeMaisFrequentadoCidade) like :p1 or upper(p.professorNome) like :p1 or upper(p.raquete) like :p1 or upper(j.nome) like :p1) and u = :p2") })
@Table(name = "jogador")
public class Jogador extends BaseEntity implements Atualizavel {
	@TextoAtualizacao
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jogador other = (Jogador) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String getTextoAtualizacao() {
		return ReflectionHelper.getTextoAtualizacao(this);
	}
}