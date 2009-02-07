package net.barragem.view.backingbean;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.util.PersistenceHelper;

public class CadastrarJogoDeBarragemBackingBean extends BaseBean {
	private JogoBarragem jogoBarragem;

	public CadastrarJogoDeBarragemBackingBean() {
		jogoBarragem = new JogoBarragem();
		jogoBarragem.inicializaParaDuplas();
	}

	public JogoBarragem getJogoBarragem() {
		return jogoBarragem;
	}

	public void setJogoBarragem(JogoBarragem jogoBarragem) {
		this.jogoBarragem = jogoBarragem;
	}

	public void salva(ActionEvent e) {
		PersistenceHelper.persiste(jogoBarragem);
	}
}