package net.barragem.business.bo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.exception.SaldoInsuficienteException;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Evento;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Parcial;
import net.barragem.persistence.entity.Placar;
import net.barragem.persistence.entity.Rodada;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PersistenceHelper;

public class EventoBo extends BaseBo {

	public EventoBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void salvaEvento(Evento evento, Jogador jogadorVencedorWo, List<JogadorEvento> jogadoresEventosExcluidos)
			throws SaldoInsuficienteException {
		if (jogadoresEventosExcluidos != null && !jogadoresEventosExcluidos.isEmpty()) {
			for (JogadorEvento jogadorEvento : jogadoresEventosExcluidos) {
				PersistenceHelper.remove(jogadorEvento);
			}
		}
		if (evento instanceof Jogo) {
			((Jogo) evento).calculaVencedorEInverteParciaisSeNecessario(jogadorVencedorWo);
			removeSetsIncompletos(((Jogo) evento).getPlacar());

			if (evento instanceof JogoBarragem) {
				if (evento.getId() == null) {
					PersistenceHelper.persiste(getContaUsuario().criaOperacaoDebitoJogoBarragem(1));
					PersistenceHelper.persiste(getContaUsuario());
					PersistenceHelper.persiste(Atualizacao.criaCriarJogoBarragem(getUsuarioLogado(),
							((JogoBarragem) evento).getRodada().getCiclo().getBarragem(), evento.getJogadoresEventos()
									.get(0).getJogador(), evento.getJogadoresEventos().get(1).getJogador()));
				} else {
					PersistenceHelper.persiste(Atualizacao.criaAtualizarJogoBarragem(getUsuarioLogado(),
							((JogoBarragem) evento).getRodada().getCiclo().getBarragem(), evento.getJogadoresEventos()
									.get(0).getJogador(), evento.getJogadoresEventos().get(1).getJogador()));
				}
			}
		}

		PersistenceHelper.persiste(evento);
	}

	public void removeSetsIncompletos(Placar placar) {
		List<Parcial> parciaisRemovidas = placar.removeSetsIncompletos();
		for (Parcial parcial : parciaisRemovidas) {
			if (parcial.getId() != null) {
				PersistenceHelper.remove(parcial);
			}
		}
	}

	public Evento removeEvento(Paginavel<Evento> paginacaoEventos, int index) {
		Evento focus = paginacaoEventos.getPosteriorImediatoOuAnteriorImediato(getIndex());
		Evento eventoRemovido = paginacaoEventos.getPagina().remove(index);
		removeEvento(eventoRemovido);

		return focus;
	}

	public void removeEvento(Rodada rodada, int index) {
		removeEvento(rodada.getJogos().remove(getIndex()));
		PersistenceHelper.persiste(rodada);
	}

	private void removeEvento(Evento evento) {
		PersistenceHelper.remove(evento);

		if (evento instanceof JogoBarragem) {
			PersistenceHelper.persiste(getContaUsuario().criaOperacaoDevolucao(1));
			PersistenceHelper.persiste(getContaUsuario());
		}
	}
}
