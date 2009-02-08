package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import net.barragem.persistence.entity.Jogador;
import net.barragem.util.PersistenceHelper;

public class LookupBean extends BaseBean {

	public List<SelectItem> getListaJogadores() {
		StringBuilder query = new StringBuilder();
		query.append("from Jogador where usuarioDono.id=");
		query.append(getUsuarioLogado().getId());
		List<SelectItem> items = new ArrayList<SelectItem>();
		Jogador jogador = null;
		for (Iterator it = PersistenceHelper.find(query.toString()).iterator(); it.hasNext();) {
			jogador = (Jogador) it.next();
			SelectItem selectItem = new SelectItem(jogador, jogador.getNome());
			items.add(selectItem);
		}
		return items;
	}
}
