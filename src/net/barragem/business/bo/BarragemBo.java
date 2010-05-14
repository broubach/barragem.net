package net.barragem.business.bo;

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
		}
		PersistenceHelper.persiste(barragem);
		if (acaoEnum != null) {
			PersistenceHelper.persiste(barragem.criaCicloERodada());
			PersistenceHelper.persiste(Atualizacao.criaCriarBarragem(getUsuarioLogado(), barragem));
		}
	}
}
