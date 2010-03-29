package net.barragem.business.bo;

import java.util.Date;

import net.barragem.persistence.entity.AcaoEnum;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Barragem;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.BaseBean;

public class BarragemBo extends BaseBean {

	private static BarragemBo instance;

	public static BarragemBo getInstance() {
		if (instance == null) {
			instance = new BarragemBo();
		}
		return instance;
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
