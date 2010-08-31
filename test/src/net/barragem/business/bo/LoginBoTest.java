package net.barragem.business.bo;

import java.util.List;

import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Rodada;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginBoTest {
	private Transaction tx = null;

	private Usuario usuario1 = null;
	private Usuario usuario2 = null;
	private Jogador jogador1 = null;
	private Jogador jogador2 = null;
	private Barragem barragem = null;
	private Rodada rodada = null;

	@Before
	public void before() {
		tx = PersistenceHelper.beginTransaction();
		PersistenceHelper.setTest(true);
		usuario1 = PersistenceHelper.findFirst(Usuario.class);
		usuario2 = PersistenceHelper.findFirst(Usuario.class, usuario1);
		jogador1 = PersistenceHelper.findFirst(Jogador.class);
		jogador2 = PersistenceHelper.findFirst(Jogador.class, jogador1);
		barragem = PersistenceHelper.findFirst(Barragem.class);
		rodada = barragem.getCiclos().get(0).getRodadas().get(0);
		criaAtualizacoesDeBarragem(usuario1, barragem, rodada);
		criaAtualizacoesDeJogoBarragem(usuario1, usuario2, barragem);
		criaAtualizacoesDeUsuario(usuario1, usuario2);
	}

	private void criaAtualizacoesDeBarragem(Usuario usuario1, Barragem barragem, Rodada rodada) {
		// Justino criou a barragem Amigos do Tennis
		PersistenceHelper.persiste(Atualizacao.criaCriarBarragem(usuario1, barragem));
		// Justino sorteou os jogos da rodada n.5 da barragem Amigos do Tennis
		PersistenceHelper.persiste(Atualizacao.criaSortearJogosBarragem(usuario1, rodada, barragem));
	}

	private void criaAtualizacoesDeJogoBarragem(Usuario usuario1, Usuario usuario2, Barragem barragem) {
		// Justino atualizou o jogo da barragem Primeira Classe (Amigos do Tennis) entre Bernardo e Marcelo
		PersistenceHelper.persiste(Atualizacao.criaAtualizarJogoBarragem(usuario1, barragem, jogador1, jogador2));
		// Justino criou um novo jogo na barragem Primeira Classe (Amigos do Tennis) entre Bernardo e Marcelo
		PersistenceHelper.persiste(Atualizacao.criaCriarJogoBarragem(usuario1, barragem, jogador1, jogador2));
	}

	private void criaAtualizacoesDeUsuario(Usuario usuario1, Usuario usuario2) {
		// Justino adicionou Bernardo Roubach à sua lista 
		PersistenceHelper.persiste(Atualizacao.criaAdicionarUsuario(usuario1, usuario2));
	}

	@Test
	public void testLogin() {
		List<Atualizacao> result = PersistenceHelper.findByNamedQueryWithLimits("atualizacaoPaginaInicialQuery", 0, 5,
				usuario2.getId());
		Assert.assertTrue(result.size() >= 1);
	}

	@After
	public void after() {
		PersistenceHelper.rollbackTransaction(tx);
	}
}
