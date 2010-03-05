package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Barragem;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;
import org.hibernate.exception.ConstraintViolationException;

@KeepAlive
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
				addMensagemAtualizacaoComSucesso();
			} catch (ConstraintViolationException e1) {
				messages.addErrorMessage("label_barragem_jah_existente", "label_barragem_jah_existente");
			}
		}
	}

	private void lista() {
		barragens = PersistenceHelper.findByNamedQuery("barragemPorUsuarioQuery", getUsuarioLogado());
	}

	public void detalhaBarragem(ActionEvent e) {
		barragemEmFoco = (Barragem) barragens.get(getIndex()).clone();
	}

	public void editaCiclo(ActionEvent e) {
		if (getIndex() != -1) {
			barragemEmFoco = (Barragem) PersistenceHelper.findByPk(Barragem.class, barragens.get(getIndex()).getId(),
					"ciclos");
		} else {
			GerirBarragemBean bean = (GerirBarragemBean) getRequestAttribute("gerirBarragemBean");
			barragemEmFoco = bean.getBarragemEmFoco();
		}
		if (barragemEmFoco.getCiclos().isEmpty()) {
			PersistenceHelper.persiste(barragemEmFoco.criaCicloERodada());
		}
		GerirCicloBean cicloBean = new GerirCicloBean();
		cicloBean.carregaUltimoCiclo(barragemEmFoco);

		setRequestAttribute("gerirCicloBean", cicloBean);
	}

	public void exibePainelBarragem(ActionEvent e) {
		Barragem barragem = (Barragem) PersistenceHelper.findByPk(Barragem.class, barragens.get(getIndex()).getId(),
				"ciclos");

		ExibirPainelBarragemBean painelBarragemBean = new ExibirPainelBarragemBean();
		painelBarragemBean.setCicloEmFoco(barragem.getCiclos().get(0));
		setRequestAttribute("exibirPainelBarragemBean", painelBarragemBean);
	}
}