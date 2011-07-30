package net.barragem.view.backingbean;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.RequisicaoRecuperarSenha;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class RecuperarSenhaBean extends BaseBean {
	private String email;
	private String novaSenha;
	private String confirmacaoNovaSenha;

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
			// soma a quantidade de todas as requisicoes fechadas
			List<Integer> totais = PersistenceHelper.findByNamedQuery("totalRequisicoesFechadasByUsuarioQuery",
					usuarios.get(0));
			String hash;
			if (totais.size() > 0) {
				hash = encriptMd5(totais.get(0) + fillLeft(usuarios.get(0).getId().toString(), 6, '0'));
			} else {
				hash = encriptMd5("0" + fillLeft(usuarios.get(0).getId().toString(), 6, '0'));
			}
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

			sendMail("no-reply@barragem.net", "barragem.net", email, "instruções para recuperação de senha",
					MessageFormat.format(emailTemplateRecuperarSenha, hash));

			messages.addInfoMessage(null, "label_email_enviado_com_sucesso");
			email = "";
		} else {
			messages.addErrorMessage("envio-instrucao", "label_true");
		}
	}

	public void recuperaSenha(ActionEvent e) {
		if (validaAlteracaoSenha()) {
			RequisicaoRecuperarSenha requisicao = (RequisicaoRecuperarSenha) PersistenceHelper.findByNamedQuery(
					"findRequisicaoByHashQuery", getRequest().getAttribute("recuperarSenhaForm:hash")).get(0);
			requisicao.getUsuario().setSenha(encriptMd5(novaSenha));
			requisicao.setDataConclusao(new Date());
			PersistenceHelper.persiste(requisicao);
			addMensagemAtualizacaoComSucesso();
			novaSenha = "";
			confirmacaoNovaSenha = "";
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