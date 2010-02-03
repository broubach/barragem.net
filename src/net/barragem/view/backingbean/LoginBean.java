package net.barragem.view.backingbean;

import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Usuario;
import net.barragem.util.PersistenceHelper;

public class LoginBean extends BaseBean {
	private String login;
	private String senha;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void login(ActionEvent e) {
		List<Usuario> usuarios = PersistenceHelper.findByNamedQuery("loginQuery", encriptMd5(senha), login);
		if (usuarios.isEmpty()) {
			messages.addErrorMessage("login", "label_login_senha_invalidos");
		} else {
			// atualiza hora do ultimo acesso
			usuarios.get(0).setDataPenultimoAcesso(usuarios.get(0).getDataUltimoAcesso());
			usuarios.get(0).setDataUltimoAcesso(new Date());
			PersistenceHelper.persiste(usuarios.get(0));
			PersistenceHelper.initialize("jogadores", usuarios.get(0));
			// coloca usuario na sessao
			setUsuarioLogado(usuarios.get(0));
			// encaminha para pagina inicial da comunidade
			sendRedirect("/protectedpages/index.xhtml");
		}
	}

	public void sair(ActionEvent e) {
		getSession().invalidate();
	}
}