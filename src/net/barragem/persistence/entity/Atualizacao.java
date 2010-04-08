package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@NamedQueries( { @NamedQuery(name = "atualizacaoPaginaInicialQuery", query = "select distinct a from Atualizacao a left outer join fetch a.predicados p, "
		+ "Barragem b "
		+ "right outer join b.ciclos c "
		+ "right outer join c.ranking r "
		+ "right outer join r.jogador j "
		+ "where (a.objetoClassName like '%.Usuario' and a.objetoId = j.usuarioCorrespondente.id and a.objetoId = :usuarioId) or "
		+ "(a.objetoClassName like '%.Barragem' and a.objetoId = b.id and j.usuarioCorrespondente.id = :usuarioId) or "
		+ "(a.objetoClassName like '%.Rodada' and a.objetoId = r.id and j.usuarioCorrespondente.id = :usuarioId) or "
		+ "(p.tipoPredicadoValue = '1' and p.predicadoValue like '%.Barragem' and p.predicadoValueId = b.id and j.usuarioCorrespondente.id = :usuarioId) or"
		+ "(p.tipoPredicadoValue = '1' and p.predicadoValue like '%.Jogador' and p.predicadoValueId = j.id and j.usuarioCorrespondente.id = :usuarioId) order by a.data desc") })
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
	@OrderBy(value = "predicado desc")
	private List<Predicado> predicados;

	@Transient
	private BaseEntity loadedSujeito;
	@Transient
	private BaseEntity loadedObjeto;

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
		this.predicados = new ArrayList<Predicado>();
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

	public List<Predicado> getPredicados() {
		return predicados;
	}

	public void setPredicados(List<Predicado> predicados) {
		this.predicados = predicados;
	}

	public BaseEntity getLoadedSujeito() {
		return loadedSujeito;
	}

	public void setLoadedSujeito(BaseEntity loadedSujeito) {
		this.loadedSujeito = loadedSujeito;
	}

	public BaseEntity getLoadedObjeto() {
		return loadedObjeto;
	}

	public void setLoadedObjeto(BaseEntity loadedObjeto) {
		this.loadedObjeto = loadedObjeto;
	}

	public static Atualizacao criaCriarBarragem(Usuario sujeito, Barragem objeto) {
		return new Atualizacao(Usuario.class.getName(), sujeito.getId(), AcaoEnum.CriarBarragem, Barragem.class
				.getName(), objeto.getId(), new Date());
	}

	public static Atualizacao criaSortearJogosBarragem(Usuario sujeito, Rodada objeto, Barragem predicado) {
		return new Atualizacao(Usuario.class.getName(), sujeito.getId(), AcaoEnum.SortearJogosBarragem, Rodada.class
				.getName(), objeto.getId(), new Date(), new Predicado("label_predicado_da_barragem", true,
				Barragem.class.getName(), TipoPredicadoValueEnum.Clazz, predicado.getId()));
	}

	public static Atualizacao criaAtualizarJogoBarragem(Usuario sujeito, Barragem objeto, Jogador predicado1,
			Jogador predicado2) {
		return new Atualizacao(Usuario.class.getName(), sujeito.getId(), AcaoEnum.AtualizarJogoBarragem, Barragem.class
				.getName(), objeto.getId(), new Date(), new Predicado("label_predicado_entre", true, Jogador.class
				.getName(), TipoPredicadoValueEnum.Clazz, predicado1.getId()), new Predicado("label_predicado_e", true,
				Jogador.class.getName(), TipoPredicadoValueEnum.Clazz, predicado2.getId()));
	}

	public static Atualizacao criaCriarJogoBarragem(Usuario sujeito, Barragem objeto, Jogador predicado1,
			Jogador predicado2) {
		return new Atualizacao(Usuario.class.getName(), sujeito.getId(), AcaoEnum.CriarJogoBarragem, Barragem.class
				.getName(), objeto.getId(), new Date(), new Predicado("label_predicado_entre", true, Jogador.class
				.getName(), TipoPredicadoValueEnum.Clazz, predicado1.getId()), new Predicado("label_predicado_e", true,
				Jogador.class.getName(), TipoPredicadoValueEnum.Clazz, predicado2.getId()));
	}

	public static Atualizacao criaAdicionarUsuario(Usuario sujeito, Usuario objeto) {
		return new Atualizacao(Usuario.class.getName(), sujeito.getId(), AcaoEnum.AdicionarUsuario, Usuario.class
				.getName(), objeto.getId(), new Date(), new Predicado("label_predicado_sua_lista", true));
	}
}