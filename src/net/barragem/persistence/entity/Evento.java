package net.barragem.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.barragem.scaffold.MessageBundleUtils;

@Entity
@NamedQuery(name = "meusEventosQuery", query = "select distinct e from Evento e join e.jogadoresEventos je where je.jogador.usuarioCorrespondente = :usuario order by e.data desc")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "evento")
public abstract class Evento extends BaseEntity implements Cloneable {

	public static final String TIPO_JOGO_BARRAGEM = "jb";
	public static final String TIPO_JOGO_AVULSO = "ja";
	public static final String TIPO_TREINO = "t";

	public static final String RESULTADO_VITORIA = "v";
	public static final String RESULTADO_DERROTA = "d";
	public static final String RESULTADO_INDEFINIDO = "i";

	public static final String CANHOTO_SIM = "s";
	public static final String CANHOTO_NAO = "n";
	public static final String CANHOTO_INDEFINIDO = "i";

	@ValidateRequired
	private Date data;

	@ValidateRequired
	private Date hora;

	@OneToMany(mappedBy = "evento", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private List<JogadorEvento> jogadoresEventos;

	@ManyToOne
	private Usuario usuarioResponsavel;

	@Transient
	private List<JogadorEvento> jogadoresEventosOrdenados;

	@Transient
	private Usuario usuarioLogado;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public List<JogadorEvento> getJogadoresEventos() {
		if (jogadoresEventos == null) {
			jogadoresEventos = new ArrayList<JogadorEvento>();
		}
		return jogadoresEventos;
	}

	public void setJogadoresEventos(List<JogadorEvento> jogadoresEventos) {
		this.jogadoresEventos = jogadoresEventos;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public Usuario getUsuarioResponsavel() {
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

	public List<JogadorEvento> getJogadoresEventosOrdenados() {
		return jogadoresEventosOrdenados;
	}

	public void setJogadoresEventosOrdenados(List<JogadorEvento> jogadoresEventosOrdenados) {
		this.jogadoresEventosOrdenados = jogadoresEventosOrdenados;
	}

	public Object clone() {
		try {
			Object newObject = getClass().newInstance();
			cloneTo(newObject);
			return newObject;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void cloneTo(Object newObject) {
		((Evento) newObject).setId(getId());
		((Evento) newObject).setData(data);
		((Evento) newObject).setHora(hora);
		((Evento) newObject).setUsuarioResponsavel(usuarioResponsavel);

		List<JogadorEvento> clonedJogadoresEventos = new ArrayList<JogadorEvento>();
		JogadorEvento clonedJogadorEvento = null;
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			clonedJogadorEvento = (JogadorEvento) ((JogadorEvento) jogadorEvento).clone();
			clonedJogadorEvento.setEvento((Evento) newObject);
			clonedJogadoresEventos.add(clonedJogadorEvento);
		}
		((Evento) newObject).setJogadoresEventos(clonedJogadoresEventos);
	}

	public abstract String getTipoLabel();

	public abstract String getJogadoresLabel();

	public abstract String getResultadoLabel();

	public abstract String getResultadoValue();

	public String getComentarioUsuarioLogado() {
		return getJogadorEventoUsuarioLogado().getComentario();
	}

	public JogadorEvento getJogadorEventoUsuarioLogado() {
		if (usuarioLogado == null) {
			throw new IllegalStateException();
		}
		for (JogadorEvento jogadorEvento : jogadoresEventos) {
			if (jogadorEvento.getJogador().getUsuarioCorrespondente() != null
			        && usuarioLogado.getId().equals(jogadorEvento.getJogador().getUsuarioCorrespondente().getId())) {
				return jogadorEvento;
			}
		}
		return null;
	}

	public abstract boolean isUsuarioLogadoVencedor();

	public abstract boolean isUsuarioLogadoPerdedor();

	public String getParticipantesCanhotosLabel() {
		if (getUsuarioLogado() == null) {
			throw new IllegalStateException();
		}
		Usuario usuario = null;
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (!jogadorEvento.getJogador().equals(getUsuarioLogado().getJogador())
			        && jogadorEvento.getJogador().getUsuarioCorrespondente() != null) {
				usuario = jogadorEvento.getJogador().getUsuarioCorrespondente();
				if (usuario.getPerfil() != null && usuario.getPerfil().getLadoForehand() != null
				        && usuario.getPerfil().getLadoForehand().equals(LadoForehandEnum.Esquerdo)) {
					return MessageBundleUtils.getInstance().get("label_sim");
				} else if (usuario.getPerfil() != null && usuario.getPerfil().getLadoForehand() != null
				        && usuario.getPerfil().getLadoForehand().equals(LadoForehandEnum.Direito)) {
					return MessageBundleUtils.getInstance().get("label_nao");
				}
			}
		}
		return MessageBundleUtils.getInstance().get("label_indefinido");
	}

	public String getParticipantesCanhotosValue() {
		if (getUsuarioLogado() == null) {
			throw new IllegalStateException();
		}
		Usuario usuario = null;
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (!jogadorEvento.getJogador().equals(getUsuarioLogado().getJogador())
			        && jogadorEvento.getJogador().getUsuarioCorrespondente() != null) {
				usuario = jogadorEvento.getJogador().getUsuarioCorrespondente();
				if (usuario.getPerfil() != null && usuario.getPerfil().getLadoForehand() != null
				        && usuario.getPerfil().getLadoForehand().equals(LadoForehandEnum.Esquerdo)) {
					return CANHOTO_SIM;
				} else if (usuario.getPerfil() != null && usuario.getPerfil().getLadoForehand() != null
				        && usuario.getPerfil().getLadoForehand().equals(LadoForehandEnum.Direito)) {
					return CANHOTO_NAO;
				}
			}
		}
		return CANHOTO_INDEFINIDO;
	}

	public abstract String getTipoValue();

	public String getJogadoresEventosStr(Jogador jogador) {
		int totalJogadores = 0;
		for (int i = 0; i < jogadoresEventos.size(); i++) {
			if (!jogadoresEventos.get(i).getJogador().equals(jogador)) {
				totalJogadores++;
			}
		}

		StringBuilder result = new StringBuilder();
		if (totalJogadores > 1) {
			int numJogadores = 0;
			for (JogadorEvento jogadorEvento : jogadoresEventos) {
				if (!jogadorEvento.getJogador().equals(jogador)) {
					result.append(jogadorEvento.getJogador().getNome());
					if (numJogadores < totalJogadores - 1) {
						numJogadores++;
						result.append(", ");
					} else {
						result.append(" e ");
					}
				}
			}
		} else {
			for (JogadorEvento jogadorEvento : jogadoresEventos) {
				if (!jogadorEvento.getJogador().equals(jogador)) {
					result.append(jogadorEvento.getJogador().getNome());
				}
			}
		}

		return result.toString();
	}
}