package net.barragem.view.backingbean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.barragem.business.bo.BarragemBo;
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
import net.barragem.view.backingbean.componentes.JogadorEventoComparatorVencedorPrimeiroBarragem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.TruePredicate;

@KeepAlive
public class ExibirEventosBean extends BaseBean implements Preparavel {

	private Usuario usuarioEmFoco;
	private Paginavel<Evento> paginacaoEventos = null;
	private String filtroData;
	private String filtroTipo;
	private Jogador filtroParticipantes;
	private String filtroPlacar;
	private String filtroVitoria;
	private String filtroCanhotos;
	private List<Evento> listaSalva = null;

	public ExibirEventosBean() {
		prepara();
	}

	private void prepara() {
		usuarioEmFoco = PersistenceHelper.findByPk(Usuario.class, getId());
		listaSalva = PersistenceHelper.findByNamedQuery("meusEventosQuery", usuarioEmFoco);
		paginacaoEventos = new PaginavelSampleImpl<Evento>(listaSalva, null, 6);

		getBo(BarragemBo.class).atribuiPontuacaoGeralAJogadoresDeJogosBarragem(paginacaoEventos.getSourceList());

		for (Evento evento : paginacaoEventos.getSourceList()) {
			if (evento instanceof Jogo) {
				PersistenceHelper.initialize("parciais", ((Jogo) evento).getPlacar());
			}
			evento.setUsuarioLogado(usuarioEmFoco);
			Collections.sort(evento.getJogadoresEventos(), new JogadorEventoComparatorVencedorPrimeiroBarragem());
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
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
		for (Evento evento : listaSalva) {
			if (evento.getData() != null) {
				SelectItem item = new SelectItem(dateFormat.format(evento.getData()), dateFormat.format(evento
				        .getData()));
				result.add(item);
			}
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

	public void filtra(ValueChangeEvent evt) {
		if (updateModelValues(evt)) {
			return;
		}

		Predicate lastPredicate = TruePredicate.getInstance();
		if (filtroData != null && !filtroData.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(MessageBundleUtils.getInstance().get("format_day_month_year"));
			sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));
			lastPredicate = AndPredicate.getInstance(lastPredicate, new FiltroDataPredicate(sdf, filtroData));
		}
		if (filtroTipo != null && !filtroTipo.equals("")) {
			lastPredicate = AndPredicate.getInstance(lastPredicate, new FiltroTipoPredicate(filtroTipo));
		}
		if (filtroParticipantes != null) {
			lastPredicate = AndPredicate.getInstance(lastPredicate, new FiltroParticipantesPredicate(
			        filtroParticipantes));
		}
		if (filtroPlacar != null && !filtroPlacar.equals("")) {
			lastPredicate = AndPredicate.getInstance(lastPredicate, new FiltroPlacarPredicate(filtroPlacar));
		}
		if (filtroVitoria != null && !filtroVitoria.equals("")) {
			lastPredicate = AndPredicate.getInstance(lastPredicate, new FiltroVitoriaPredicate(filtroVitoria));
		}
		if (filtroCanhotos != null && !filtroCanhotos.equals("")) {
			lastPredicate = AndPredicate.getInstance(lastPredicate, new FiltroCanhotosPredicate(filtroCanhotos));
		}

		List<Evento> listaFiltrada = new ArrayList<Evento>(listaSalva);
		CollectionUtils.filter(listaFiltrada, lastPredicate);

		Collections.sort(listaFiltrada, new EventoMaisRecenteComparator());
		paginacaoEventos = new PaginavelSampleImpl<Evento>(listaFiltrada, null, 6);
	}

	@Override
	public void prepara(ActionEvent e) {
		prepara();
	}
}

class FiltroCanhotosPredicate implements Predicate {
	private String filtroCanhotos;

	public FiltroCanhotosPredicate(String filtroCanhotos) {
		this.filtroCanhotos = filtroCanhotos;
	}

	@Override
	public boolean evaluate(Object evento) {
		if (((Evento) evento).getParticipantesCanhotosValue().equals(filtroCanhotos)) {
			return true;
		}
		return false;
	}

}

class FiltroVitoriaPredicate implements Predicate {
	private String filtroVitoria;

	public FiltroVitoriaPredicate(String filtroVitoria) {
		this.filtroVitoria = filtroVitoria;
	}

	@Override
	public boolean evaluate(Object evento) {
		if (((Evento) evento).getResultadoValue().equals(filtroVitoria)) {
			return true;
		}
		return false;
	}

}

class FiltroPlacarPredicate implements Predicate {
	private String filtroPlacar;

	public FiltroPlacarPredicate(String filtroPlacar) {
		this.filtroPlacar = filtroPlacar;
	}

	@Override
	public boolean evaluate(Object evento) {
		if (evento instanceof Jogo && ((Jogo) evento).getPlacar().getContagemDeParciais().equals(filtroPlacar)) {
			return true;
		}
		return false;
	}

}

class FiltroParticipantesPredicate implements Predicate {
	private Jogador filtroParticipantes;

	public FiltroParticipantesPredicate(Jogador filtroParticipantes) {
		this.filtroParticipantes = filtroParticipantes;
	}

	@Override
	public boolean evaluate(Object evento) {
		if (((Evento) evento).getJogadoresLabel().contains(filtroParticipantes.getNome())) {
			return true;
		}
		return false;
	}

}

class FiltroTipoPredicate implements Predicate {
	private String filtroTipo;

	public FiltroTipoPredicate(String filtroTipo) {
		this.filtroTipo = filtroTipo;
	}

	@Override
	public boolean evaluate(Object evento) {
		if (((Evento) evento).getTipoValue().equals(filtroTipo)) {
			return true;
		}
		return false;
	}

}

class FiltroDataPredicate implements Predicate {
	private String filtroData;
	private SimpleDateFormat sdf;

	public FiltroDataPredicate(SimpleDateFormat sdf, String filtroData) {
		this.sdf = sdf;
		this.filtroData = filtroData;
	}

	@Override
	public boolean evaluate(Object evento) {
		if (((Evento) evento).getData() != null && filtroData.equals(sdf.format(((Evento) evento).getData()))) {
			return true;
		}
		return false;
	}

}