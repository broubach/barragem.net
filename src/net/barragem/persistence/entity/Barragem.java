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
		@NamedQuery(name = "pesquisaBarragemDeUsuarioQuery", query = "select distinct b from Barragem b join b.ciclos c join c.ranking r where (upper(b.categoria.nome) like :p1 or upper(b.local) like :p1) and r.jogador.usuarioCorrespondente = :p2"),
		@NamedQuery(name = "pesquisaBarragemQuery", query = "from Barragem b where upper(b.categoria.nome) like :p or upper(b.local) like :p"),
		@NamedQuery(name = "cicloNomeEBarragemQuery", query = "select c.nome, c.nomeAlternativoBaseadoEmAno, b.local, b.categoria.nome from Barragem b join b.ciclos c where b.id = :id and c.id = (select max(ciclo.id) from Ciclo ciclo join ciclo.barragem barragem where barragem.id = :id)"),
		@NamedQuery(name = "cicloNomeEBarragemComRodadasQuery", query = "select b.id, c.nome, c.nomeAlternativoBaseadoEmAno, b.local, b.categoria.nome, size(c.rodadas) from Barragem b join b.ciclos c where b.id = :id and c.id = (select max(ciclo.id) from Ciclo ciclo join ciclo.barragem barragem where barragem.id = :id)") })
@Table(name = "barragem")
public class Barragem extends BaseEntity implements Validatable, Cloneable, Atualizavel {

	@ManyToOne
	@ValidateRequired
	@TextoAtualizacao(parentesis = true)
	private Categoria categoria;

	@ValidateRequired
	@TextoAtualizacao
	private String local;

	private ParametroCiclo parametrosIniciais = new ParametroCiclo();

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario administrador;

	@OneToMany(mappedBy = "barragem")
	private List<Ciclo> ciclos;

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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Ciclo criaCicloERodada() {
		return criaCicloERodada(Calendar.getInstance().get(Calendar.YEAR));
	}

	public Ciclo criaCicloERodada(Integer nomeAlternativoBaseadoEmAno) {
		if (getCiclos() == null) {
			setCiclos(new ArrayList<Ciclo>());
		}
		Ciclo ciclo = new Ciclo();
		ciclo.setNomeAlternativoBaseadoEmAno(nomeAlternativoBaseadoEmAno);
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