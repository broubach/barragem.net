package net.barragem.business.bo;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Conta;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

public class LoginBo extends BaseBo {

	public LoginBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void login(Usuario usuario) {
		// atualiza hora do ultimo acesso
		usuario.setDataPenultimoAcesso(usuario.getDataUltimoAcesso());
		usuario.setDataUltimoAcesso(new Date());
		PersistenceHelper.persiste(usuario);
		PersistenceHelper.initialize("jogadores", usuario);
		// coloca usuario na sessao
		setUsuarioLogado(usuario);
		// coloca conta do usuario na sessao
		setContaUsuario((Conta) PersistenceHelper.findByNamedQuery("findContaPorUsuarioQuery", usuario).get(0));

		// busca por atualizacoes
		PersistenceHelper.findByNamedQueryWithLimits("atualizacaoPaginaInicialQuery", 0, 5, getUsuarioLogado().getId());
	}
}
