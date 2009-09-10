package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "barragemPorUsuarioQuery", query = "from Barragem barragem where barragem.administrador = :usuario")
@Table(name = "barragem")
public class Barragem extends BaseEntity {

	private String nome;
	private String local;
	private ParametroCiclo parametrosIniciais = new ParametroCiclo();

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario administrador;

	@OneToMany(mappedBy = "barragem")
	private List<Ciclo> ciclos;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public ParametroCiclo getParametrosIniciais() {
		return parametrosIniciais;
	}

	public void setParametrosIniciais(ParametroCiclo parametrosIniciais) {
		this.parametrosIniciais = parametrosIniciais;
	}

	public Usuario getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Usuario administrador) {
		this.administrador = administrador;
	}

	public List<Ciclo> getCiclos() {
		return ciclos;
	}

	public void setCiclos(List<Ciclo> ciclos) {
		this.ciclos = ciclos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((local == null) ? 0 : local.hashCode());
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
		Barragem other = (Barragem) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		if (local == null) {
			if (other.local != null)
				return false;
		} else if (!local.equals(other.local))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	public Ciclo criaCicloERodada() {
		Ciclo ciclo = new Ciclo();
		ciclo.setNomeAlternativoBaseadoEmMes(Calendar.getInstance().get(Calendar.MONTH));
		ciclo.setParametros(getParametrosIniciais());
		ciclo.setRodadas(new ArrayList<Rodada>());
		ciclo.adicionaRodada();
		getCiclos().add(ciclo);
		ciclo.setBarragem(this);
		return ciclo;
	}

	public Integer getIndiceNaLista(Ciclo ciclo) {
		for (int i = 0; i < ciclos.size(); i++) {
			if (ciclos.get(i).equals(ciclo)) {
				return i;
			}
		}
		return null;
	}
}