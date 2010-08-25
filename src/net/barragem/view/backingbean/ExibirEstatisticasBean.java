package net.barragem.view.backingbean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.barragem.persistence.entity.Evento;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.JogadorEvento;
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
	private String filtroData;
	private String filtroTipo;
	private Jogador filtroParticipantes;
	private String filtroPlacar;
	private String filtroVitoria;
	private String filtroCanhotos;
	private List<Evento> listaSalva = null;

	public ExibirEstatisticasBean() {
		usuarioEmFoco = PersistenceHelper.findByPk(Usuario.class, getId());
		listaSalva = PersistenceHelper.findByNamedQuery("meusEventosQuery", usuarioEmFoco);
		paginacaoEventos = new PaginavelSampleImpl<Evento>(listaSalva);
		prepara();
	}

	private void prepara() {
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

	public String getFiltroData() {
		return filtroData;
	}

	public void setFiltroData(String filtroData) {
		this.filtroData = filtroData;
	}

	public String getFiltroTipo() {
		return filtroTipo;
	}

	public void setFiltroTipo(String filtroTipo) {
		this.filtroTipo = filtroTipo;
	}

	public Jogador getFiltroParticipantes() {
		return filtroParticipantes;
	}

	public void setFiltroParticipantes(Jogador filtroParticipantes) {
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

	public String getFiltroCanhotos() {
		return filtroCanhotos;
	}

	public void setFiltroCanhotos(String filtroCanhotos) {
		this.filtroCanhotos = filtroCanhotos;
	}

	public boolean getUsuarioJahAdicionado() {
		return getUsuarioLogado().hasJogador(usuarioEmFoco);
	}

	public List<SelectItem> getListaDatas() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(MessageBundleUtils.getInstance()
				.get("format_day_month_year"));
		for (Evento evento : listaSalva) {
			SelectItem item = new SelectItem(dateFormat.format(evento.getData()), dateFormat.format(evento.getData()));
			result.add(item);
		}
		return result;
	}

	public List<SelectItem> getListaParticipantes() {
		Set<Jogador> participantes = new HashSet<Jogador>();
		for (Evento evento : listaSalva) {
			for (JogadorEvento jogadorEvento : evento.getJogadoresEventos()) {
				participantes.add(jogadorEvento.getJogador());
			}
		}
		participantes.remove(usuarioEmFoco.getJogador());
		List<SelectItem> result = new ArrayList<SelectItem>();
		for (Jogador jogador : participantes) {
			SelectItem item = new SelectItem(jogador, jogador.getNome());
			result.add(item);
		}
		return result;
	}

	public List<SelectItem> getListaPlacares() {
		Set<String> contagemDeParciais = new HashSet<String>();
		for (Evento evento : listaSalva) {
			if (evento instanceof Jogo && ((Jogo) evento).possuiVencedores()) {
				contagemDeParciais.add(((Jogo) evento).getPlacar().getContagemDeParciais());
			}
		}
		List<SelectItem> result = new ArrayList<SelectItem>();
		for (String contagemParcial : contagemDeParciais) {
			SelectItem item = new SelectItem(contagemParcial, contagemParcial);
			result.add(item);
		}
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

	public void filtra(ValueChangeEvent arg0) {
		List<Evento> eventosFiltrados = new ArrayList<Evento>();
		SimpleDateFormat sdf = new SimpleDateFormat(MessageBundleUtils.getInstance().get("format_day_month_year"));
		for (Evento evento : listaSalva) {
			if (filtroData != null && !filtroData.equals("")) {
				if (filtroData.equals(sdf.format(evento.getData()))) {
					eventosFiltrados.add(evento);
				}
			}
			if (filtroTipo != null && !filtroTipo.equals("")) {
				if (evento.getTipoStr().equals(filtroTipo)) {
					eventosFiltrados.add(evento);
				}
			}
			if (filtroParticipantes != null) {
				if (evento.getJogadoresStr().contains(filtroParticipantes.getNome())) {
					eventosFiltrados.add(evento);
				}
			}
			if (filtroPlacar != null && !filtroPlacar.equals("")) {
				if (evento instanceof Jogo && ((Jogo) evento).getPlacar().getContagemDeParciais().equals(filtroPlacar)) {
					eventosFiltrados.add(evento);
				}

			}
			if (filtroVitoria != null && !filtroVitoria.equals("")) {
				if (evento.getResultadoStr().equals(filtroVitoria)) {
					eventosFiltrados.add(evento);
				}
			}
			if (filtroCanhotos != null && !filtroCanhotos.equals("")) {
				if (evento.getParticipantesCanhotos().equals(filtroCanhotos)) {
					eventosFiltrados.add(evento);
				}
			}
		}

		paginacaoEventos = new PaginavelSampleImpl<Evento>(eventosFiltrados);
	}
}
