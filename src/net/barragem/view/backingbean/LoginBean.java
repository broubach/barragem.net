package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.LoginBo;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Predicado;
import net.barragem.persistence.entity.Rodada;
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

	public String detalhaAtualizacao() {
		Atualizacao atualizacao = getAtualizacoes().get(getIndex());
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
			ExibirPainelBarragemBean painelBarragemBean = new ExibirPainelBarragemBean();
			Ciclo cicloEmFoco = null;
			if (detalhe instanceof Barragem) {
				PersistenceHelper.initialize("ciclos", detalhe);
				cicloEmFoco = ((Barragem) detalhe).getCiclos().get(0);
				for (Ciclo ciclo : ((Barragem) detalhe).getCiclos()) {
					PersistenceHelper.initialize("rodadas", ciclo);
				}
			} else if (detalhe instanceof Rodada) {
				PersistenceHelper.initialize("ciclos", ((Rodada) detalhe).getCiclo().getBarragem());
				for (Ciclo ciclo : ((Rodada) detalhe).getCiclo().getBarragem().getCiclos()) {
					PersistenceHelper.initialize("rodadas", ciclo);
				}
				painelBarragemBean.setNumeroRodada(((Rodada) detalhe).getNumero());
				int index = ((Rodada) detalhe).getCiclo().getBarragem().getCiclos().indexOf(
						((Rodada) detalhe).getCiclo());
				cicloEmFoco = ((Rodada) detalhe).getCiclo().getBarragem().getCiclos().get(index);
			}
			painelBarragemBean.setCicloEmFoco(cicloEmFoco);
			painelBarragemBean.visualizaRodada(null);
			setRequestAttribute("exibirPainelBarragemBean", painelBarragemBean);

			return "exibirPainelBarragem";
		}

		return "";
	}
}