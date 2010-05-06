package net.barragem.business.bo;

import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Mensagem;
import net.barragem.scaffold.PersistenceHelper;

public class MensagemBo extends BaseBo {

	public MensagemBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void responde(Mensagem mensagem, String textoResposta) {
		Mensagem resposta = new Mensagem();
		resposta.setData(new Date());
		resposta.setDestinatario(mensagem.getRemetente());
		resposta.setMensagem(textoResposta);
		resposta.setRemetente(getUsuarioLogado());
		PersistenceHelper.persiste(mensagem);

		sendMail("no-reply@barragem.net", resposta.getRemetente().getNomeCompletoCapital(), resposta.getDestinatario()
				.getEmail(), "barragem.net - nova mensagem", MessageFormat.format(emailNovaMensagem, resposta
				.getRemetente().getNomeCompletoCapital()));
	}
}
