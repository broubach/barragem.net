package net.barragem.persistence.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@NamedQueries( {
		@NamedQuery(name = "atualizacaoUsuarioPaginaInicialQuery", query = "from Atualizacao a where objetoClassName like '%.Usuario' and objetoId = :objetoId order by a.data desc"),
		@NamedQuery(name = "atualizacaoBarragemPaginaInicialQuery", query = "from Atualizacao a where objetoClassName like '%.Barragem' and objetoId in (:objetoId) order by a.data desc"),
		@NamedQuery(name = "atualizacaoJogoBarragemPaginaInicialQuery", query = "from Atualizacao a where objetoClassName like '%.Jogo' and objetoId in (:objetoId) order by a.data desc") })
@Table(name = "atualizacao")
/***
 * Ex. de frases de atualização:
 * <a>Bernardo</a> incluiu em sua lista de jogadores <a>Justino Caetano</a>
 * Bernardo: sujeito
 * incluiu em sua lista de jogadores: ação
 * Justino Caetano: objeto
 * 
 * <a>Bernardo</a> sorteou os jogos da barragem <a>Primeira Classe (Amigos do Tennis)</a>
 * Bernardo: sujeito
 * sorteou os jogos da barragem: ação
 * Primeira Classe (Amigos do Tennis): objeto
 * 
 * <a>Bernardo</a> atualizou o jogo da barragem <a>Primeira Classe (Amigos do Tennis)</a> entre Justino e Marcelo 
 * Bernardo: sujeito
 * atualizou o jogo da barragem : ação
 * Primeira Classe (Amigos do Tennis): objeto
 * entre Justino e Marcelo: predicado
 * 
 * <a>Bernardo</a> atualizou o nome da barragem <a>Primeira Classe (Amigos do Tennis)</a> 
 * <a>Justino</a> atualizou sua foto <a>foto</a>
 * <a>Justino</a> atualizou o clube onde joga em seu perfil 
 */
public class Atualizacao extends BaseEntity {

	private String sujeitoClassName;
	private Integer sujeitoId;
	private AcaoEnum acao;
	private String objetoClassName;
	private Integer objetoId;
	private Date data;

	@OneToMany(mappedBy = "atualizacao", cascade = { CascadeType.ALL })
	@OrderBy(value = "id")
	private Set<Predicado> predicados;

	public Atualizacao() {
	}

	public Atualizacao(String sujeitoClassName, Integer sujeitoId, AcaoEnum acao, String objetoClassName,
			Integer objetoId, Date data) {
		this.sujeitoClassName = sujeitoClassName;
		this.sujeitoId = sujeitoId;
		this.acao = acao;
		this.objetoClassName = objetoClassName;
		this.objetoId = objetoId;
		this.data = data;
	}

	public Atualizacao(String sujeitoClassName, Integer sujeitoId, AcaoEnum acao, String objetoClassName,
			Integer objetoId, Date data, Predicado... predicados) {
		this(sujeitoClassName, sujeitoId, acao, objetoClassName, objetoId, data);
		this.predicados = new HashSet<Predicado>();
		for (Predicado predicado : predicados) {
			predicado.setAtualizacao(this);
			this.predicados.add(predicado);
		}
	}

	public String getSujeitoClassName() {
		return sujeitoClassName;
	}

	public void setSujeitoClassName(String sujeitoClassName) {
		this.sujeitoClassName = sujeitoClassName;
	}

	public Integer getSujeitoId() {
		return sujeitoId;
	}

	public void setSujeitoId(Integer sujeitoId) {
		this.sujeitoId = sujeitoId;
	}

	public AcaoEnum getAcao() {
		return acao;
	}

	public void setAcao(AcaoEnum acao) {
		this.acao = acao;
	}

	public String getObjetoClassName() {
		return objetoClassName;
	}

	public void setObjetoClassName(String objetoClassName) {
		this.objetoClassName = objetoClassName;
	}

	public Integer getObjetoId() {
		return objetoId;
	}

	public void setObjetoId(Integer objetoId) {
		this.objetoId = objetoId;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Set<Predicado> getPredicados() {
		return predicados;
	}

	public void setPredicados(Set<Predicado> predicados) {
		this.predicados = predicados;
	}
}
