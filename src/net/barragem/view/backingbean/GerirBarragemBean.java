package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.BarragemBo;
import net.barragem.persistence.entity.Barragem;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;
import org.hibernate.exception.ConstraintViolationException;

@KeepAlive
public class GerirBarragemBean extends BaseBean {
	private Barragem barragemEmFoco;
	private List<Barragem> barragensQueAdministro;
	private List<Barragem> barragensQueParticipo;

	public GerirBarragemBean() {
		lista();
	}

	public List<Barragem> getBarragensQueAdministro() {
		return barragensQueAdministro;
	}

	public List<Barragem> getBarragensQueParticipo() {
		return barragensQueParticipo;
	}

	public void removeBarragem(ActionEvent e) {
		Barragem barragemARemover = barragensQueAdministro.get(getIndex());
		PersistenceHelper.remove(barragemARemover);
		barragensQueAdministro.remove(barragemARemover);
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
				getBo(BarragemBo.class).salva(barragemEmFoco);
				lista();
				addMensagemAtualizacaoComSucesso();
			} catch (ConstraintViolationException e1) {
				messages.addErrorMessage("label_barragem_jah_existente", "label_barragem_jah_existente");
			}
		}
	}

	private void lista() {
		barragensQueAdministro = PersistenceHelper.findByNamedQuery("barragensQueAdministroQuery", getUsuarioLogado());
		barragensQueParticipo = PersistenceHelper.findByNamedQuery("barragensQueParticipoQuery", getUsuarioLogado());
	}

	public void detalhaBarragem(ActionEvent e) {
		barragemEmFoco = (Barragem) barragensQueAdministro.get(getIndex()).clone();
	}

	public void editaCiclo(ActionEvent e) {
		if (getIndex() != -1) {
			barragemEmFoco = (Barragem) PersistenceHelper.findByPk(Barragem.class, barragensQueAdministro.get(
					getIndex()).getId(), "ciclos");
		} else {
			GerirBarragemBean bean = (GerirBarragemBean) getRequestAttribute("gerirBarragemBean");
			barragemEmFoco = bean.getBarragemEmFoco();
		}
		GerirCicloBean cicloBean = new GerirCicloBean();
		cicloBean.carregaUltimoCiclo(barragemEmFoco);

		setRequestAttribute("gerirCicloBean", cicloBean);
	}

	public void exibePainelBarragem(ActionEvent e) {
		Barragem barragem = (Barragem) PersistenceHelper.findByPk(Barragem.class, barragensQueAdministro
				.get(getIndex()).getId(), "ciclos");

		ExibirPainelBarragemBean painelBarragemBean = new ExibirPainelBarragemBean();
		painelBarragemBean.setCicloEmFoco(barragem.getCiclos().get(0));
		setRequestAttribute("exibirPainelBarragemBean", painelBarragemBean);
	}
}