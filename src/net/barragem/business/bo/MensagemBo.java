package net.barragem.business.bo;

import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Mensagem;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

public class MensagemBo extends BaseBo {

	public MensagemBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public Mensagem responde(Mensagem mensagem, String textoResposta) {
		return envia(mensagem.getRemetente(), textoResposta);
	}

	public Mensagem envia(Usuario destinatario, String texto) {
		Mensagem mensagem = new Mensagem();
		mensagem.setData(new Date());
		mensagem.setDestinatario(destinatario);
		mensagem.setMensagem(texto);
		mensagem.setRemetente(getUsuarioLogado());
		PersistenceHelper.persiste(mensagem);

		sendMail("no-reply@barragem.net", mensagem.getRemetente().getNomeCompletoCapital(), mensagem.getDestinatario()
				.getEmail(), "barragem.net - nova mensagem", MessageFormat.format(emailNovaMensagem, mensagem
				.getRemetente().getNomeCompletoCapital()));
		return mensagem;
	}
}