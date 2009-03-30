package net.barragem.view.backingbean;

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
		List<Usuario> usuarios = PersistenceHelper.findByNamedQuery("loginQuery", senha, login);
		if (usuarios.isEmpty()) {
			setErrorMessage("error bruto");
		} else {
			// coloca usuario na sessao
			setUsuarioLogado(usuarios.get(0));
			// encaminha para pagina inicial da comunidade
			sendRedirect("/pages/gerirbarragem/gerirBarragem.xhtml");
		}
	}
}