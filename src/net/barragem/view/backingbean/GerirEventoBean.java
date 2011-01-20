package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.barragem.business.bo.BarragemBo;
import net.barragem.business.bo.EventoBo;
import net.barragem.business.bo.JogadorBo;
import net.barragem.exception.SaldoInsuficienteException;
import net.barragem.persistence.entity.Evento;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.JogadorJogo;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Parcial;
import net.barragem.persistence.entity.Placar;
import net.barragem.persistence.entity.Treino;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.JogadorEventoComparatorVencedorPrimeiroBarragem;
import net.barragem.view.backingbean.componentes.JogadorEventoUsuarioLogadoComparator;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.beanutils.BeanComparator;

@KeepAlive
public class GerirEventoBean extends BaseBean {
    private static final Integer TREINO = new Integer(2);
    private static final Integer JOGO_BARRAGEM = new Integer(3);
    private static final Integer JOGO_AVULSO = new Integer(1);
    private Integer tipo;
    private Evento eventoEmFoco;
    private Paginavel<Evento> paginacaoEventos;
    private Jogador jogadorVencedorWo;
    private String comentario;
    private List<JogadorEvento> jogadoresEventosExcluidos;
    private String jogadorNome;

    public Evento getEventoEmFoco() {
        return eventoEmFoco;
    }

    public void setEventoEmFoco(Evento eventoEmFoco) {
        this.eventoEmFoco = eventoEmFoco;
    }

    public Paginavel<Evento> getPaginacaoEventos() {
        return paginacaoEventos;
    }

    public void setPaginacaoEventos(Paginavel<Evento> paginacaoEventos) {
        this.paginacaoEventos = paginacaoEventos;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Jogador getJogadorVencedorWo() {
        return jogadorVencedorWo;
    }

    public void setJogadorVencedorWo(Jogador jogadorVencedorWo) {
        this.jogadorVencedorWo = jogadorVencedorWo;
    }

    public GerirEventoBean() {
        paginacaoEventos = new PaginavelSampleImpl<Evento>(obtemMeusEventos());
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getJogadorNome() {
        return jogadorNome;
    }

    public void setJogadorNome(String jogadorNome) {
        this.jogadorNome = jogadorNome;
    }

    public void removeEvento(ActionEvent e) {
        getBo(EventoBo.class).removeEvento(paginacaoEventos, getIndex());
        addMensagemAtualizacaoComSucesso();
        paginacaoEventos = new PaginavelSampleImpl<Evento>(obtemMeusEventos(), paginacaoEventos.getPaginaAtual(), null);
    }

    public void salvaEvento(ActionEvent e) {
        if (valida(eventoEmFoco)) {
            eventoEmFoco.getJogadorEventoUsuarioLogado().setComentario(comentario);
            try {
                getBo(EventoBo.class).salvaEvento(eventoEmFoco, jogadorVencedorWo, jogadoresEventosExcluidos);
            } catch (SaldoInsuficienteException e1) {
                messages.addErrorMessage("saldo_insuficiente_exception", "label_saldo_insuficiente");
            }
            if (eventoEmFoco instanceof Jogo) {
                ((Jogo) eventoEmFoco).getPlacar().completaSetsSeNecessario(3);
            }
            paginacaoEventos = new PaginavelSampleImpl<Evento>(obtemMeusEventos());
            addMensagemAtualizacaoComSucesso();
        }
    }

    private boolean valida(Evento evento) {
        for (JogadorEvento jogadorEvento : evento.getJogadoresEventos()) {
            if (jogadorEvento.getJogador() == null) {
                messages.addErrorMessage(null, "error_selecione_os_jogadores_do_evento");
            }
        }
        if (!messages.getErrorMessages().isEmpty()) {
            return false;
        }
        return true;
    }

    private List<Evento> obtemMeusEventos() {
        List<Evento> meusEventos = PersistenceHelper.findByNamedQuery("meusEventosQuery", getUsuarioLogado());

        getBo(BarragemBo.class).atribuiPontuacaoGeralAJogadoresDeJogosBarragem(meusEventos);

        List<Integer> idsPlacar = new ArrayList<Integer>();
        for (Evento evento : meusEventos) {
            evento.setUsuarioLogado(getUsuarioLogado());
            if (evento instanceof Jogo) {
                idsPlacar.add(((Jogo) evento).getPlacar().getId());
            }
        }
        if (!idsPlacar.isEmpty()) {
            List<Placar> placares = PersistenceHelper.findByNamedQuery("placarFetchParcialQuery", idsPlacar);
            Map<Integer, Placar> placaresPorId = new HashMap<Integer, Placar>();
            for (Placar placar : placares) {
                placaresPorId.put(placar.getId(), placar);
            }
            for (Evento evento : meusEventos) {
                if (evento instanceof Jogo) {
                    ((Jogo) evento).setPlacar(placaresPorId.get(((Jogo) evento).getPlacar().getId()));
                    Collections.sort(evento.getJogadoresEventos(),
                            new JogadorEventoComparatorVencedorPrimeiroBarragem());
                } else if (evento instanceof Treino) {
                    Collections.sort(evento.getJogadoresEventos(), new JogadorEventoUsuarioLogadoComparator(
                            getUsuarioLogado()));
                }
            }
        }
        return meusEventos;
    }

    public void detalhaEvento(ActionEvent e) {
        eventoEmFoco = (Evento) paginacaoEventos.getPagina().get(getIndex()).clone();
        if (eventoEmFoco instanceof JogoBarragem) {
            ((Jogo) eventoEmFoco).getPlacar().completaSetsSeNecessario(3);
            tipo = JOGO_BARRAGEM;
        } else if (eventoEmFoco instanceof Treino) {
            tipo = TREINO;
        } else if (eventoEmFoco instanceof Jogo) {
            ((Jogo) eventoEmFoco).getPlacar().completaSetsSeNecessario(3);
            tipo = JOGO_AVULSO;
        }
        eventoEmFoco.setUsuarioLogado(getUsuarioLogado());
        comentario = eventoEmFoco.getComentarioUsuarioLogado();
        jogadoresEventosExcluidos = new ArrayList<JogadorEvento>();
    }

    public void novoEvento(ActionEvent e) {
        tipo = JOGO_AVULSO;
        eventoEmFoco = new Jogo();
        eventoEmFoco.setUsuarioLogado(getUsuarioLogado());
        eventoEmFoco.setUsuarioResponsavel(getUsuarioLogado());
        ((Jogo) eventoEmFoco).setPlacar(new Placar());
        ((Jogo) eventoEmFoco).getPlacar().setParciais(new ArrayList<Parcial>());
        ((Jogo) eventoEmFoco).getPlacar().completaSetsSeNecessario(3);
        inicializaJogadoresEvento(JogadorJogo.class);
        comentario = null;
    }

    public void alteraTipo(ActionEvent e) {
        if (tipo.equals(JOGO_AVULSO)) {
            eventoEmFoco = new Jogo();
            ((Jogo) eventoEmFoco).setPlacar(new Placar());
            ((Jogo) eventoEmFoco).getPlacar().setParciais(new ArrayList<Parcial>());
            ((Jogo) eventoEmFoco).getPlacar().completaSetsSeNecessario(3);
            inicializaJogadoresEvento(JogadorJogo.class);
        } else {
            eventoEmFoco = new Treino();
            inicializaJogadoresEvento(JogadorEvento.class);
        }
        eventoEmFoco.setUsuarioLogado(getUsuarioLogado());
        eventoEmFoco.setUsuarioResponsavel(getUsuarioLogado());
    }

    private void inicializaJogadoresEvento(Class<?> clazz) {
        try {
            JogadorEvento jogadorEvento = (JogadorEvento) clazz.newInstance();
            jogadorEvento.setJogador(getUsuarioLogado().getJogador());
            jogadorEvento.setEvento(eventoEmFoco);
            List<JogadorEvento> jogadoresEventos = new ArrayList<JogadorEvento>();
            jogadoresEventos.add(jogadorEvento);
            jogadorEvento = (JogadorEvento) clazz.newInstance();
            jogadorEvento.setEvento(eventoEmFoco);
            jogadoresEventos.add(jogadorEvento);
            eventoEmFoco.setJogadoresEventos(jogadoresEventos);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SelectItem> getListaJogadoresSelecionados() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        JogadorEvento jogadorEvento = null;
        for (Iterator<JogadorEvento> it = eventoEmFoco.getJogadoresEventos().iterator(); it.hasNext();) {
            jogadorEvento = it.next();
            if (jogadorEvento.getJogador() != null) {
                SelectItem selectItem = new SelectItem(jogadorEvento.getJogador(), jogadorEvento.getJogador().getNome());
                items.add(selectItem);
            }
        }
        return items;
    }

    public List<SelectItem> getListaJogadoresHabilitados() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        List<Jogador> jogadores = PersistenceHelper.findByNamedQuery("jogadoresPorUsuarioDonoQuery", "%",
                getUsuarioLogado().getId());
        Collections.sort(jogadores, new BeanComparator("nome"));
        Jogador jogador = null;
        for (Iterator<Jogador> it = jogadores.iterator(); it.hasNext();) {
            jogador = it.next();
            SelectItem selectItem = new SelectItem(jogador, jogador.getNome());
            items.add(selectItem);
        }
        return items;
    }

    public void adicionaJogador(ActionEvent e) {
        JogadorEvento jogadorEvento = new JogadorEvento();
        jogadorEvento.setEvento(eventoEmFoco);
        eventoEmFoco.getJogadoresEventos().add(jogadorEvento);
    }

    public void removeJogador(ActionEvent e) {
        JogadorEvento jogadorEventoExcluido = eventoEmFoco.getJogadoresEventos().remove(getIndex());
        if (jogadorEventoExcluido.getId() != null) {
            jogadoresEventosExcluidos.add(jogadorEventoExcluido);
        }
    }

    public void criaJogador(ActionEvent e) {
        if (validaNovo()) {
            getBo(JogadorBo.class).adicionaJogador(jogadorNome);

            addMensagemAtualizacaoComSucesso();
            jogadorNome = null;
        }
    }

    private boolean validaNovo() {
        if (jogadorNome == null || jogadorNome.isEmpty()) {
            messages.addErrorMessage(null, "label_digite_o_nome_do_novo_jogador");
        } else if (getUsuarioLogado().jahPossuiJogador(jogadorNome)) {
            messages.addErrorMessage(null, "label_jogador_com_mesmo_nome_jah_existente");
        }
        return messages.getErrorMessages().isEmpty();
    }
}