package net.barragem.persistence.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.barragem.scaffold.ReflectionHelper;
import net.barragem.scaffold.ValidatableSampleImpl;

import org.apache.commons.beanutils.BeanUtils;

@Entity
@NamedQueries( {
		@NamedQuery(name = "barragensQueAdministroQuery", query = "from Barragem barragem where barragem.administrador = :usuario"),
		@NamedQuery(name = "barragensQueParticipoQuery", query = "select distinct barragem from Barragem barragem join barragem.ciclos ciclo join ciclo.ranking ranking where ranking.jogador.usuarioCorrespondente = :usuario"),
		@NamedQuery(name = "pesquisaBarragemDeUsuarioQuery", query = "select distinct b from Barragem b join b.ciclos c join c.ranking r where (upper(b.nome) like :p1 or upper(b.local) like :p1) and r.jogador.usuarioCorrespondente = :p2"),
		@NamedQuery(name = "pesquisaBarragemQuery", query = "from Barragem b where upper(b.nome) like :p or upper(b.local) like :p") })
@Table(name = "barragem")
public class Barragem extends BaseEntity implements Validatable, Cloneable, Atualizavel {

	@ValidateRequired
	@TextoAtualizacao
	private String nome;

	@ValidateRequired
	@TextoAtualizacao(parentesis = true)
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

	public Ciclo criaCicloERodada() {
		return criaCicloERodada(Calendar.getInstance().get(Calendar.MONTH));
	}

	public Ciclo criaCicloERodada(Integer nomeAlternativoBaseadoEmMes) {
		if (getCiclos() == null) {
			setCiclos(new ArrayList<Ciclo>());
		}
		Ciclo ciclo = new Ciclo();
		ciclo.setNomeAlternativoBaseadoEmMes(nomeAlternativoBaseadoEmMes);
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

	public List<String> validate() {
		List<String> result = new ArrayList<String>();
		result.addAll(new ValidatableSampleImpl(this).validate());
		result.addAll(parametrosIniciais.validate());
		return result;
	}

	public Object clone() {
		try {
			Barragem result = (Barragem) BeanUtils.cloneBean(this);
			result.setParametrosIniciais((ParametroCiclo) parametrosIniciais.clone());
			return result;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getTextoAtualizacao() {
		return ReflectionHelper.getTextoAtualizacao(this);
	}
}