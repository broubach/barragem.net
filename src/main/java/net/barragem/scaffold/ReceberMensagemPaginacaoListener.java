package net.barragem.scaffold;

import java.util.ArrayList;
import java.util.List;

import net.barragem.persistence.entity.Mensagem;
import net.barragem.view.backingbean.ReceberMensagemBean;
import net.barragem.view.backingbean.componentes.TextoEFlagPrivadoDto;

public class ReceberMensagemPaginacaoListener implements PaginacaoListener<Mensagem> {

	private ReceberMensagemBean receberMensagemBean;

	public ReceberMensagemPaginacaoListener(ReceberMensagemBean receberMensagemBean) {
		this.receberMensagemBean = receberMensagemBean;
	}

	@Override
	public void afterPageChange(Paginavel<Mensagem> paginavel) {
		List<TextoEFlagPrivadoDto> respostas = new ArrayList<TextoEFlagPrivadoDto>();
		TextoEFlagPrivadoDto resposta = null;
		for (int i = 0; i < paginavel.getPagina().size(); i++) {
			resposta = new TextoEFlagPrivadoDto();
			resposta.setPrivada(paginavel.getPagina().get(i).isPrivada());
			respostas.add(resposta);

		}
		receberMensagemBean.setRespostas(respostas);
	}
}
