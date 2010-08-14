package net.barragem.business.bo;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Jogador;
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
		PersistenceHelper.persiste(Atualizacao.criaAdicionarUsuario(getUsuarioLogado(), jogadorDesvinculado
				.getUsuarioCorrespondente()));
		sendMail("no-reply@barragem.net", getUsuarioLogado().getNomeCompletoCapital(), jogadorDesvinculado
				.getUsuarioCorrespondente().getEmail(),
				"barragem.net - você foi adicionado(a) à lista de jogadores do(a) " + getUsuarioLogado().getNome(),
				MessageFormat.format(emailTemplateAdicaoJogador, getUsuarioLogado().getNomeCompletoCapital()));

		PersistenceHelper.persiste(jogadorDesvinculado);
	}

	public void atualizaNomesDeTodosJogadores(Usuario usuarioCorrespondente, String nomeCompletoCapital) {
		PersistenceHelper.execute("atualizaNomesJogadoresQuery", usuarioCorrespondente.getId(), nomeCompletoCapital);
	}

}
