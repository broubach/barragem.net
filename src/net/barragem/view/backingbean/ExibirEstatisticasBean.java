package net.barragem.view.backingbean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import net.barragem.persistence.entity.Evento;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.MessageBundleUtils;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.EventoMaisRecenteComparator;
import net.barragem.view.backingbean.componentes.JogadorEventoComparatorVencedorPrimeiro;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirEstatisticasBean extends BaseBean {

	public static final String FILTRO_VITORIA_V = "v";
	public static final String FILTRO_VITORIA_D = "d";
	public static final String FILTRO_VITORIA_I = "i";

	private Usuario usuarioEmFoco;
	private Paginavel<Evento> paginacaoEventos = null;
	private Date filtroData;
	private String filtroTipo;
	private String filtroParticipantes;
	private String filtroPlacar;
	private String filtroVitoria;
	private Boolean filtroCanhotos;

	public ExibirEstatisticasBean() {
		usuarioEmFoco = PersistenceHelper.findByPk(Usuario.class, getId());
		paginacaoEventos = new PaginavelSampleImpl<Evento>(PersistenceHelper.findByNamedQuery("meusEventosQuery",
				usuarioEmFoco));
		for (Evento evento : paginacaoEventos.getSourceList()) {
			if (evento instanceof Jogo) {
				PersistenceHelper.initialize("parciais", ((Jogo) evento).getPlacar());
			}
			evento.setUsuarioLogado(usuarioEmFoco);
			Collections.sort(evento.getJogadoresEventos(), new JogadorEventoComparatorVencedorPrimeiro());
		}
		Collections.sort(paginacaoEventos.getSourceList(), new EventoMaisRecenteComparator());
	}

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public Paginavel<Evento> getPaginacaoEventos() {
		return paginacaoEventos;
	}

	public void setPaginacaoEventos(Paginavel<Evento> paginacaoEventos) {
		this.paginacaoEventos = paginacaoEventos;
	}

	public Date getFiltroData() {
		return filtroData;
	}

	public void setFiltroData(Date filtroData) {
		this.filtroData = filtroData;
	}

	public String getFiltroTipo() {
		return filtroTipo;
	}

	public void setFiltroTipo(String filtroTipo) {
		this.filtroTipo = filtroTipo;
	}

	public String getFiltroParticipantes() {
		return filtroParticipantes;
	}

	public void setFiltroParticipantes(String filtroParticipantes) {
		this.filtroParticipantes = filtroParticipantes;
	}

	public String getFiltroPlacar() {
		return filtroPlacar;
	}

	public void setFiltroPlacar(String filtroPlacar) {
		this.filtroPlacar = filtroPlacar;
	}

	public String getFiltroVitoria() {
		return filtroVitoria;
	}

	public void setFiltroVitoria(String filtroVitoria) {
		this.filtroVitoria = filtroVitoria;
	}

	public Boolean getFiltroCanhotos() {
		return filtroCanhotos;
	}

	public void setFiltroCanhotos(Boolean filtroCanhotos) {
		this.filtroCanhotos = filtroCanhotos;
	}

	public boolean getUsuarioJahAdicionado() {
		return getUsuarioLogado().hasJogador(usuarioEmFoco);
	}

	public List<SelectItem> getListaDatas() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(MessageBundleUtils.getInstance()
				.get("format_day_month_year"));
		for (Evento evento : paginacaoEventos.getSourceList()) {
			SelectItem item = new SelectItem(evento.getData(), dateFormat.format(evento.getData()));
			result.add(item);
		}
		return result;
	}

	public List<SelectItem> getListaParticipantes() {
		List<SelectItem> result = new ArrayList<SelectItem>();

		return result;
	}

	public List<SelectItem> getListaPlacares() {
		List<SelectItem> result = new ArrayList<SelectItem>();

		return result;
	}

	public int getVitorias() {
		int vitorias = 0;
		for (Evento evento : paginacaoEventos.getSourceList()) {
			evento.setUsuarioLogado(usuarioEmFoco);
			if (evento.isUsuarioLogadoVencedor()) {
				vitorias++;
			}
		}
		return vitorias;
	}

	public int getDerrotas() {
		int derrotas = 0;
		for (Evento evento : paginacaoEventos.getSourceList()) {
			evento.setUsuarioLogado(usuarioEmFoco);
			if (evento.isUsuarioLogadoPerdedor()) {
				derrotas++;
			}
		}
		return derrotas;
	}
}
