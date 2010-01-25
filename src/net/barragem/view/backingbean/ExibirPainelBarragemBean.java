package net.barragem.view.backingbean;

import net.barragem.persistence.entity.Ciclo;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirPainelBarragemBean extends BaseBean {

	private Ciclo cicloEmFoco;

	public Ciclo getCicloEmFoco() {
		return cicloEmFoco;
	}

	public void setCicloEmFoco(Ciclo cicloEmFoco) {
		this.cicloEmFoco = cicloEmFoco;
	}
}
