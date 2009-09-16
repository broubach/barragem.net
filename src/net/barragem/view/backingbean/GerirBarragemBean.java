package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.util.PersistenceHelper;

//@KeepAlive
public class GerirBarragemBean extends BaseBean {
	private Barragem barragemEmFoco;
	private List<Barragem> barragens;

	public GerirBarragemBean() {
		barragens = PersistenceHelper.findByNamedQuery("barragemPorUsuarioQuery", getUsuarioLogado());
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
		PersistenceHelper.persiste(barragemEmFoco);
		if (!barragens.contains(barragemEmFoco)) {
			barragens.add(barragemEmFoco);
		}

		addInfoMessage(null, "label_informacao_atualizada_com_sucesso");
		FacesContext.getCurrentInstance().getMaximumSeverity();
	}

	public void detalhaBarragem(ActionEvent e) {
		barragemEmFoco = (Barragem) PersistenceHelper.findByPk(Barragem.class, barragens.get(getIndex()).getId());
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