package net.barragem.business.bo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.exception.SaldoInsuficienteException;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Rodada;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.JogoBarragemComparator;

public class RodadaBo extends BaseBo {

	public RodadaBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void sorteiaJogos(Rodada rodada) throws SaldoInsuficienteException {
		PersistenceHelper.initialize("ranking", rodada.getCiclo());
		List<CicloJogador> jogadores = new ArrayList<CicloJogador>(rodada.getCiclo().getRanking());
		CicloJogador.removeJogadoresQuePossuemJogos(jogadores, rodada.getJogadoresDosJogos());

		// remove jogadores que não estejam habilitados
		for (Iterator<CicloJogador> it = jogadores.iterator(); it.hasNext();) {
			CicloJogador cicloJogador = it.next();
			if (!cicloJogador.getHabilitado()) {
				it.remove();
			}
		}

		// verifica se saldo é suficiente
		if (!getContaUsuario().possuiSaldoSuficiente(jogadores.size() / 2)) {
			throw new SaldoInsuficienteException();
		}

		// sorteia jogos
		List<JogoBarragem> jogosSorteados = new ArrayList<JogoBarragem>();
		while (jogadores.size() >= 2) {
			int posicaoJogadorSorteado = rodada.getCiclo().sorteiaBaseadoNoRaio(jogadores.size());
			jogosSorteados.add(rodada.criaJogoBarragem(jogadores.get(0).getJogador(), jogadores.get(
					posicaoJogadorSorteado).getJogador()));

			jogadores.remove(posicaoJogadorSorteado);
			jogadores.remove(0);
		}
		rodada.getJogos().addAll(jogosSorteados);

		// cria operacao de debito e atualiza saldo da conta
		PersistenceHelper.persiste(getContaUsuario().criaOperacaoDebitoJogoBarragem(jogosSorteados.size()));
		PersistenceHelper.persiste(getContaUsuario());

		PersistenceHelper.persiste(rodada);
		Collections.sort(rodada.getJogos(), new JogoBarragemComparator());

		PersistenceHelper.persiste(Atualizacao.criaSortearJogosBarragem(getUsuarioLogado(), rodada, rodada.getCiclo()
				.getBarragem()));
		for (CicloJogador cicloJogador : jogadores) {
			if (cicloJogador.getJogador().getUsuarioCorrespondente() != null) {
				sendMail("no-reply@barragem.net", getUsuarioLogado().getNomeCompletoCapital(), cicloJogador
						.getJogador().getUsuarioCorrespondente().getEmail(), new StringBuilder().append(
						"barragem.net - sorteio da barragem ").append(rodada.getCiclo().getBarragem().getNome())
						.append(" realizado").toString(), MessageFormat.format(emailTemplateSorteioBarragem, rodada
						.getCiclo().getBarragem().getNome()));
			}
		}
	}

}