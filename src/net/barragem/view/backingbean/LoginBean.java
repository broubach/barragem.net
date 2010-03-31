package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.LoginBo;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

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

	public String login() {
		List<Usuario> usuarios = PersistenceHelper.findByNamedQuery("loginQuery", encriptMd5(senha), login);
		if (usuarios.isEmpty()) {
			messages.addErrorMessage("login", "label_login_senha_invalidos");
			return "";
		} else {
			getBo(LoginBo.class).login(usuarios.get(0));
			return "login";
		}
	}

	public void sair(ActionEvent e) {
		getSession().invalidate();
	}
}