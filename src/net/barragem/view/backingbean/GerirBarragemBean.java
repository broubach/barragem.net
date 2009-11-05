package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.util.PersistenceHelper;

import org.hibernate.exception.ConstraintViolationException;

public class GerirBarragemBean extends BaseBean {
	private Barragem barragemEmFoco;
	private List<Barragem> barragens;

	public GerirBarragemBean() {
		lista();
	}

	public List<Barragem> getBarragens() {
		return barragens;
	}

	public void removeBarragem(ActionEvent e) {
		Barragem barragemARemover = barragens.get(getIndex());
		PersistenceHelper.remove(barragemARemover);
		barragens.remove(barragemARemover);
	}

	public Barragem getBarragemEmFoco() {
		return barragemEmFoco;
	}

	public void setBarragemEmFoco(Barragem barragemEmFoco) {
		this.barragemEmFoco = barragemEmFoco;
	}

	public void novaBarragem(ActionEvent e) {
		barragemEmFoco = new Barragem();
		barragemEmFoco.setAdministrador(getUsuarioLogado());
	}

	public void salvaBarragem(ActionEvent e) {
		if (valida(barragemEmFoco)) {
			try {
				PersistenceHelper.persiste(barragemEmFoco);
				lista();
				messages.addInfoMessage(null, "label_informacao_atualizada_com_sucesso");
			} catch (ConstraintViolationException e1) {
				messages.addErrorMessage("label_barragem_jah_existente", "label_barragem_jah_existente");
			}
		}
	}

	private void lista() {
		barragens = PersistenceHelper.findByNamedQuery("barragemPorUsuarioQuery", getUsuarioLogado());
	}

	private boolean valida(Barragem barragemEmFoco) {
		for (String clientId : barragemEmFoco.validate()) {
			messages.addErrorMessage(clientId, "label_true");
		}

		return messages.getErrorMessages().isEmpty();
	}

	public void detalhaBarragem(ActionEvent e) {
		barragemEmFoco = (Barragem) barragens.get(getIndex()).clone();
	}

	public void editaCiclo(ActionEvent e) {
		barragemEmFoco = (Barragem) PersistenceHelper.findByPk(Barragem.class, barragens.get(getIndex()).getId(),
				"ciclos");
		if (barragemEmFoco.getCiclos().isEmpty()) {
			PersistenceHelper.persiste(barragemEmFoco.criaCicloERodada());
		}
		GerirCicloBean cicloBean = new GerirCicloBean();
		cicloBean.carregaUltimoCiclo(barragemEmFoco);

		setRequestAttribute("gerirCicloBean", cicloBean);
	}
}