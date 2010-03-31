package net.barragem.business.bo;

import java.util.Date;
import java.util.List;

import net.barragem.persistence.entity.AcaoEnum;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Barragem;
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
		criaSeisAtualizacoesDeBarragem();
		criaSeisAtualizacoesDeUsuario();
		criaSeisAtualizacoesDeJogoBarragem();
	}

	private void criaSeisAtualizacoesDeBarragem() {
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarBarragem, Barragem.class
				.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.AtualizarBarragem,
				Barragem.class.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.AtualizarBarragem,
				Barragem.class.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.AtualizarBarragem,
				Barragem.class.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.AtualizarBarragem,
				Barragem.class.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.AtualizarBarragem,
				Barragem.class.getName(), 1, new Date()));
	}

	private void criaSeisAtualizacoesDeJogoBarragem() {
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.SortearJogosBarragem,
				Barragem.class.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarJogoBarragem,
				Barragem.class.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.AtualizarJogoBarragem,
				Barragem.class.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarJogoBarragem,
				Barragem.class.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.AtualizarJogoBarragem,
				Barragem.class.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarJogoBarragem,
				Barragem.class.getName(), 1, new Date()));
	}

	private void criaSeisAtualizacoesDeUsuario() {
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarBarragem, Barragem.class
				.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarBarragem, Barragem.class
				.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarBarragem, Barragem.class
				.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarBarragem, Barragem.class
				.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarBarragem, Barragem.class
				.getName(), 1, new Date()));
		PersistenceHelper.persiste(new Atualizacao(Usuario.class.getName(), 115, AcaoEnum.CriarBarragem, Barragem.class
				.getName(), 1, new Date()));
	}

	@Test
	public void testLogin() {
		List<Atualizacao> result = PersistenceHelper.findByNamedQueryWithLimits("atualizacaoUsuarioPaginaInicialQuery",
				0, 5, 113);
		Assert.assertTrue(result.size() == 5);

		result = PersistenceHelper.findByNamedQueryWithLimits("atualizacaoBarragemPaginaInicialQuery", 0, 5, 113);
		Assert.assertTrue(result.size() == 5);

		result = PersistenceHelper.findByNamedQueryWithLimits("atualizacaoJogoBarragemPaginaInicialQuery", 0, 5, 113);
		Assert.assertTrue(result.size() == 5);
	}

	@After
	public void after() {
		PersistenceHelper.rollbackTransaction(tx);
	}
}
