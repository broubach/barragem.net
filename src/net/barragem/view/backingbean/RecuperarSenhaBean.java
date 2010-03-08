package net.barragem.view.backingbean;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.RequisicaoRecuperarSenha;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

public class RecuperarSenhaBean extends BaseBean {
	private String email;
	private String novaSenha;
	private String confirmacaoNovaSenha;
	private static final String emailTemplate = new StringBuilder()
			.append("<p>Olá,</p>")
			.append(
					"<p>Visite <a href=\"https://www.barragem.net/publicpages/recuperarsenha/recuperarSenha.xhtml?hash={0}\">esta página</a> para recuperar a sua senha. Esta página será válida por 2 dias.</p>")
			.append("<p>Atenciosamente,<br />Equipe barragem.net</p>").toString();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmacaoNovaSenha() {
		return confirmacaoNovaSenha;
	}

	public void setConfirmacaoNovaSenha(String confirmacaoNovaSenha) {
		this.confirmacaoNovaSenha = confirmacaoNovaSenha;
	}

	public void enviaInstrucoes(ActionEvent e) {
		List<Usuario> usuarios = PersistenceHelper.findByNamedQuery("recuperarSenhaQuery", email);
		if (!usuarios.isEmpty()) {
			String hash = encriptMd5(fillLeft(usuarios.get(0).getId().toString(), 8, '0'));
			Calendar calendar = Calendar.getInstance();
			calendar.roll(Calendar.DATE, -2);
			List<RequisicaoRecuperarSenha> requisicoes = PersistenceHelper.findByNamedQuery(
					"findRequisicaoValidaByHashQuery", calendar.getTime(), hash);
			RequisicaoRecuperarSenha requisicao = null;
			if (requisicoes.size() <= 0) {
				requisicao = new RequisicaoRecuperarSenha();
				requisicao.setUsuario(usuarios.get(0));
				requisicao.setHash(hash);
			} else {
				requisicao = requisicoes.get(0);
			}
			requisicao.setData(new Date());
			PersistenceHelper.persiste(requisicao);

			sendMail("no-reply@barragem.net", email, "barragem.net - instruções para recuperação de senha",
					MessageFormat.format(emailTemplate, hash));
		} else {
			messages.addErrorMessage(null, "error_o_email_informado_nao_foi_encontrado");
		}
	}

	public void recuperaSenha(ActionEvent e) {
		if (validaAlteracaoSenha()) {
			RequisicaoRecuperarSenha requisicao = (RequisicaoRecuperarSenha) PersistenceHelper.findByNamedQuery(
					"findRequisicaoValidaByHashQuery", getRequest().getParameter("hash")).get(0);
			requisicao.getUsuario().setSenha(encriptMd5(novaSenha));
			requisicao.setDataConclusao(new Date());
			PersistenceHelper.persiste(requisicao);
			addMensagemAtualizacaoComSucesso();
		}
	}

	private boolean validaAlteracaoSenha() {
		if (!novaSenha.equals(confirmacaoNovaSenha)) {
			messages.addErrorMessage(null, "label_as_novas_senhas_digitadas_nao_conferem");
			return false;
		}
		if (!validaSenha(null, novaSenha)) {
			return false;
		}
		return true;
	}
}