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

	public Mensagem responde(Mensagem mensagem, String textoResposta, Boolean privada) {
		return envia(mensagem.getRemetente(), textoResposta, privada);
	}

	public Mensagem envia(Usuario destinatario, String texto, Boolean privada) {
		Mensagem mensagem = new Mensagem();
		mensagem.setData(new Date());
		mensagem.setDestinatario(destinatario);
		mensagem.setMensagem(texto);
		mensagem.setRemetente(getUsuarioLogado());
		mensagem.setPrivada(privada);
		PersistenceHelper.persiste(mensagem);

		sendMail("no-reply@barragem.net", mensagem.getRemetente().getNomeCompletoCapital(), mensagem.getDestinatario()
				.getEmail(), "barragem.net - nova mensagem", MessageFormat.format(emailNovaMensagem, mensagem
				.getRemetente().getNomeCompletoCapital()));
		return mensagem;
	}
}