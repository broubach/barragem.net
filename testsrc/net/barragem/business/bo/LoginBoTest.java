package net.barragem.business.bo;

import java.util.Date;
import java.util.List;

import net.barragem.persistence.entity.AcaoEnum;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Predicado;
import net.barragem.persistence.entity.Rodada;
import net.barragem.persistence.entity.TipoPredicadoValueEnum;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginBoTest {
	private Transaction tx = null;

	@Before
	public void before() {
		tx = PersistenceHelper.beginTransaction();
		PersistenceHelper.setTest(true);
		criaAtualizacoesDeBarragem();
		criaAtualizacoesDeUsuario();
		criaAtualizacoesDeJogoBarragem();
	}

	private void criaAtualizacoesDeBarragem() {
		// Justino criou a barragem Amigos do Tennis
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarBarragem, Barragem.class
				.getName(), 1, new Date()));

		// Justino sorteou os jogos rodada n.5 da barragem Amigos do Tennis
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.SortearJogosBarragem,
				Rodada.class.getName(), 1, new Date(), new Predicado("label_predicado_da_barragem", true,
						Barragem.class.getName(), TipoPredicadoValueEnum.Clazz, 1)));
	}

	private void criaAtualizacoesDeJogoBarragem() {
		// Justino atualizou o jogo da barragem Primeira Classe (Amigos do Tennis) entre Bernardo e Marcelo
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.AtualizarJogoBarragem,
				Barragem.class.getName(), 1, new Date(), new Predicado("label_predicado_entre", true, Usuario.class
						.getName(), TipoPredicadoValueEnum.Clazz, 1), new Predicado("label_predicado_e", true,
						Usuario.class.getName(), TipoPredicadoValueEnum.Clazz, 1)));
		// Justino criou um novo jogo na barragem Primeira Classe (Amigos do Tennis) entre Bernardo e Marcelo
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarJogoBarragem,
				Barragem.class.getName(), 1, new Date(), new Predicado("label_predicado_entre", true, Usuario.class
						.getName(), TipoPredicadoValueEnum.Clazz, 1), new Predicado("label_predicado_e", true,
						Usuario.class.getName(), TipoPredicadoValueEnum.Clazz, 1)));
	}

	private void criaAtualizacoesDeUsuario() {
		// Justino adicionou Bernardo Roubach à sua lista 
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.AdicionarUsuario,
				Barragem.class.getName(), 1, new Date(), new Predicado("label_predicado_sua_lista", true)));
	}

	@Test
	public void testLogin() {
		List<Atualizacao> result = PersistenceHelper.findByNamedQueryWithLimits("atualizacaoUsuarioPaginaInicialQuery",
				0, 5, 113);
		Assert.assertTrue(result.size() >= 2);

		result = PersistenceHelper.findByNamedQueryWithLimits("atualizacaoBarragemPaginaInicialQuery", 0, 5, 113);
		Assert.assertTrue(result.size() >= 2);

		result = PersistenceHelper.findByNamedQueryWithLimits("atualizacaoJogoBarragemPaginaInicialQuery", 0, 5, 113);
		Assert.assertTrue(result.size() >= 1);
	}

	@After
	public void after() {
		PersistenceHelper.rollbackTransaction(tx);
	}
}
