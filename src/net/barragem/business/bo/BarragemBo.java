package net.barragem.business.bo;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.AcaoEnum;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Barragem;
import net.barragem.scaffold.PersistenceHelper;

public class BarragemBo extends BaseBo {

	public BarragemBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void salva(Barragem barragem) {
		AcaoEnum acaoEnum = null;
		if (barragem.getId() == null) {
			acaoEnum = AcaoEnum.CriarBarragem;
		} else {
			acaoEnum = AcaoEnum.AtualizarBarragem;
		}
		PersistenceHelper.persiste(barragem);
		PersistenceHelper.persiste(new Atualizacao(getUsuarioLogado().getClass().getName(), getUsuarioLogado().getId(),
				acaoEnum, barragem.getClass().getName(), barragem.getId(), new Date()));
	}
}
