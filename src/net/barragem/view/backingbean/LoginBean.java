package net.barragem.view.backingbean;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.LoginBo;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Predicado;
import net.barragem.persistence.entity.Rodada;
import net.barragem.persistence.entity.Usuario;

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
		if (getBo(LoginBo.class).autentica(login, senha)) {
			return "login";
		}
		messages.addErrorMessage("login", "label_login_senha_invalidos");
		return "";
	}

	public void sair(ActionEvent e) {
		getSession().invalidate();
	}

	public String detalhaAtualizacao() {
		Atualizacao atualizacao = getAtualizacoes().getPagina().get(getIndex());
		Object detalhe = null;
		if (getRequest().getParameter("tipo").equals("sujeito")) {
			detalhe = atualizacao.getLoadedSujeito();
		} else if (getRequest().getParameter("tipo").equals("objeto")) {
			detalhe = atualizacao.getLoadedObjeto();
		} else if (getRequest().getParameter("tipo").startsWith("predicado")) {
			int indexPredicado = Integer.parseInt(getRequest().getParameter("tipo").substring(9));
			Predicado predicado = atualizacao.getPredicados().get(indexPredicado);
			detalhe = predicado.getLoadedPredicado();
		}

		if (detalhe instanceof Usuario || detalhe instanceof Jogador) {
			ExibirPerfilBean exibirPerfilBean = new ExibirPerfilBean();
			if (detalhe instanceof Usuario) {
				exibirPerfilBean.exibePerfil((Usuario) detalhe);
			} else if (detalhe instanceof Jogador) {
				exibirPerfilBean.exibePerfil(((Jogador) detalhe).getUsuarioCorrespondente());
			}
			setRequestAttribute("exibirPerfilBean", exibirPerfilBean);
			return "exibirPerfil";

		} else if (detalhe instanceof Barragem || detalhe instanceof Rodada) {
			if (detalhe instanceof Barragem) {
				setRequestAttribute("id", ((Barragem) detalhe).getId());
			} else if (detalhe instanceof Rodada) {
				setRequestAttribute("id", ((Rodada) detalhe).getCiclo().getBarragem().getId());
			}

			return "exibirPainelBarragem";
		}

		return "";
	}
}