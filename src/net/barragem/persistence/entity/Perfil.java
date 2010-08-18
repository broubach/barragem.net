package net.barragem.persistence.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.barragem.scaffold.MessageBundleUtils;

import org.apache.commons.beanutils.BeanUtils;

@Entity
@NamedQuery(name = "perfilFetchFotoQuery", query = "select perfil, foto.id, foto.tamanho from Perfil perfil join perfil.foto foto where perfil.id in (:lista)")
@Table(name = "perfil")
public class Perfil extends BaseEntity implements Cloneable {

	private String clubeMaisFrequentadoNome;
	private String clubeMaisFrequentadoCidade;
	private Boolean professor;
	private String professorNome;
	private Date dataInicioPratica;
	private LadoForehandEnum ladoForehand;
	private TipoBackhandEnum tipoBackhand;
	private String raquete;
	private String hash;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Arquivo foto;

	@OneToOne(cascade = { CascadeType.ALL })
	private Usuario usuario;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "perfilcategoria", joinColumns = { @JoinColumn(name = "perfil_id") }, inverseJoinColumns = { @JoinColumn(name = "categoria_id") })
	private List<Categoria> categorias;

	public Perfil() {
	}

	public Perfil(Arquivo foto) {
		this.foto = foto;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
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

	public String getRaquete() {
		return raquete;
	}

	public void setRaquete(String raquete) {
		this.raquete = raquete;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hashFoto) {
		this.hash = hashFoto;
	}

	public String getJogoHa() {
		if (dataInicioPratica == null) {
			return null;
		}
		Float meses = getNumeroMeses();
		if (meses <= 365) {
			return String.valueOf(new Float(meses / 30.0f).intValue());
		}

		Float anos = meses / 365.0f;
		return String.valueOf(anos.intValue());
	}

	private Float getNumeroMeses() {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(dataInicioPratica);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long msInicial = calendar.getTimeInMillis();

		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long msFinal = calendar.getTimeInMillis();

		Float meses = (((((msFinal - msInicial) / 1000.0f/* ms */) / 60.0f/* sec */) / 60.0f/* min */) / 24.0f/* hou */);
		return meses;
	}

	public String getJogoHaUnidadeKey() {
		if (dataInicioPratica == null) {
			return null;
		}
		Float meses = getNumeroMeses();
		if (meses <= 365) {
			return "label_meses";
		}

		return "label_anos";
	}

	public String getCsCategorias() {
		if (categorias != null && !categorias.isEmpty()) {
			StringBuilder stb = new StringBuilder();
			for (Categoria categoria : categorias) {
				stb.append(MessageBundleUtils.getInstance().get(categoria.getNome())).append(", ");
			}
			return stb.toString().substring(0, stb.toString().length() - 2);
		}
		return "";
	}

	@Override
	public Object clone() {
		try {
			Perfil perfil = (Perfil) BeanUtils.cloneBean(this);
			if (perfil.getCategorias() != null) {
				perfil.setCategorias(new ArrayList<Categoria>(getCategorias()));
			}
			return perfil;
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
}