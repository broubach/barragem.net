package net.barragem.business.bo;

import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Bonus;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Rodada;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

public class JogadorBo extends BaseBo {

	public JogadorBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void vinculaJogador(Jogador jogadorDesvinculado) {
		Jogador jogador = PersistenceHelper.findByPk(Jogador.class, getId());

		jogadorDesvinculado.setNome(jogador.getNome());
		jogadorDesvinculado.setUsuarioCorrespondente(jogador.getUsuarioCorrespondente());
		PersistenceHelper.persiste(Atualizacao.criaAdicionarUsuario(getUsuarioLogado(),
		        jogadorDesvinculado.getUsuarioCorrespondente()));
		sendMail("no-reply@barragem.net", getUsuarioLogado().getNomeCompletoCapital(), jogadorDesvinculado
		        .getUsuarioCorrespondente().getEmail(),
		        "barragem.net - voc� foi adicionado(a) � lista de jogadores do(a) " + getUsuarioLogado().getNome(),
		        MessageFormat.format(emailTemplateAdicaoJogador, getUsuarioLogado().getNomeCompletoCapital()));

		PersistenceHelper.persiste(jogadorDesvinculado);
	}

	public void atualizaNomesDeTodosJogadores(Usuario usuarioCorrespondente, String nomeCompletoCapital) {
		PersistenceHelper.execute("atualizaNomesJogadoresQuery", usuarioCorrespondente.getId(), nomeCompletoCapital);
	}

	public void marcaJogadoresNaoAdicionadosAUsuarioLogado(List<Jogador> jogadores) {
		for (Jogador jogador : jogadores) {
			if (getUsuarioLogado().getJogadores().contains(jogador)) {
				jogador.setJahAdicionado(true);
			}
		}
	}

	public Jogador adicionaUsuario(Usuario usuarioAAdicionar) {
		return adiciona(usuarioAAdicionar.getNomeCompletoCapital(), usuarioAAdicionar);
	}

	public Jogador adicionaJogador(String jogadorNome) {
		return adiciona(jogadorNome, null);
	}

	private Jogador adiciona(String jogadorNome, Usuario usuarioAAdicionar) {
		Jogador novoJogador = new Jogador();
		novoJogador.setNome(jogadorNome);
		novoJogador.setUsuarioCorrespondente(usuarioAAdicionar);
		novoJogador.setUsuarioDono(getUsuarioLogado());
		getUsuarioLogado().getJogadores().add(novoJogador);
		PersistenceHelper.persiste(novoJogador);
		PersistenceHelper.persiste(getUsuarioLogado());

		if (usuarioAAdicionar != null) {
			PersistenceHelper.persiste(Atualizacao.criaAdicionarUsuario(getUsuarioLogado(), usuarioAAdicionar));

			sendMail("no-reply@barragem.net", getUsuarioLogado().getNomeCompletoCapital(),
			        usuarioAAdicionar.getEmail(), "barragem.net - voc� foi adicionado(a) � lista de jogadores do(a) "
			                + getUsuarioLogado().getNome(),
			        MessageFormat.format(emailTemplateAdicaoJogador, getUsuarioLogado().getNomeCompletoCapital()));
		}
		return novoJogador;
	}

	public void congelaJogador(CicloJogador cicloJogador, boolean... persiste) {
		for (Rodada rodada : cicloJogador.getCiclo().getRodadas()) {
			PersistenceHelper.initialize("jogos", rodada);
			if (!rodada.getFechada() && rodada.getJogadoresDosJogos().contains(cicloJogador.getJogador())) {
				cicloJogador.setCongelado(false);
				break;
			} else {
				cicloJogador.setCongelado(true);
			}
		}

		if (cicloJogador.isCongelado()) {
			cicloJogador.setPontuacaoCongeladaExcedente(null);
			cicloJogador.setPontuacaoCongelada(cicloJogador.getPontuacao());
			cicloJogador.setPontuacao(0);
			cicloJogador.getCiclo().recalculaRanking();
		}
		if (persiste == null || persiste.length == 0 || persiste[0] == true) {
			PersistenceHelper.persiste(cicloJogador.getCiclo());
		}
	}

	private Integer calculaPontosAnterioresARodadasDeHistoricoMantidas(CicloJogador cicloJogador) {
		int countRodadas = 0;
		int countPontuacaoRodadas = 0;
		Rodada rodada = null;
		for (int i = cicloJogador.getCiclo().getRodadas().size() - 1; i >= 0
		        && countRodadas < cicloJogador.getCiclo().getParametros()
		                .getRodadasDeHistoricoMantidasParaCalculoDoRanking(); i--) {
			rodada = cicloJogador.getCiclo().getRodadas().get(i);
			PersistenceHelper.initialize("jogos", rodada);
			PersistenceHelper.initialize("bonuses", rodada);
			if (rodada.getFechada()) {
				countRodadas++;
				countPontuacaoRodadas += rodada.getJogadorJogoBarragem(cicloJogador.getJogador()) != null
				        && rodada.getJogadorJogoBarragem(cicloJogador.getJogador()).getPontuacaoObtida() != null ? rodada
				        .getJogadorJogoBarragem(cicloJogador.getJogador()).getPontuacaoObtida() : 0;

				for (Bonus bonus : rodada.getBonuses()) {
					if (cicloJogador.getJogador().equals(bonus.getJogador())) {
						countPontuacaoRodadas += bonus.getValor();
						break;
					}
				}
			}
		}
		return Math.max(cicloJogador.getPontuacaoCongelada() - countPontuacaoRodadas, 0);
	}

	public void descongelaJogador(CicloJogador cicloJogador) {
		cicloJogador.setPontuacao(cicloJogador.getPontuacaoCongelada());
		cicloJogador.setPontuacaoCongeladaExcedente(calculaPontosAnterioresARodadasDeHistoricoMantidas(cicloJogador));
		cicloJogador.setPontuacaoCongelada(null);
		cicloJogador.setCongelado(false);
		cicloJogador.getCiclo().recalculaRanking();
		PersistenceHelper.persiste(cicloJogador.getCiclo());
	}
}
