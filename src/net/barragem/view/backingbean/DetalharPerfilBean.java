package net.barragem.view.backingbean;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.DetalhePerfil;
import net.barragem.util.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class DetalharPerfilBean extends BaseBean {

	public DetalhePerfil detalhePerfilEmFoco = new DetalhePerfil();

	public DetalhePerfil getDetalhePerfilEmFoco() {
		return detalhePerfilEmFoco;
	}

	public void setDetalhePerfilEmFoco(DetalhePerfil detalhePerfilEmFoco) {
		this.detalhePerfilEmFoco = detalhePerfilEmFoco;
	}

	public void salva(ActionEvent e) {
		detalhePerfilEmFoco.setUsuario(getUsuarioLogado());
		getUsuarioLogado().setDetalhePerfil(detalhePerfilEmFoco);

		PersistenceHelper.persiste(detalhePerfilEmFoco);
	}
}