package net.barragem.view.backingbean.componentes;

import java.util.List;

public class MestreDetalheImpl<K, T> implements Mestre<K>, Detalhe<T> {

	private K mestre;
	private T detalheEmFoco;
	private List<T> detalhes;

	public K getMestre() {
		return mestre;
	}

	public void setMestre(K mestre) {
		this.mestre = mestre;
	}

	public void setDetalheEmFoco(T detalheEmFoco) {
		this.detalheEmFoco = detalheEmFoco;
	}

	public T getDetalheEmFoco() {
		return detalheEmFoco;
	}

	public List<T> getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(List<T> detalhes) {
		this.detalhes = detalhes;
	}

	public Integer getIndiceNosDetalhes() {
		for (int i = 0; i < detalhes.size(); i++) {
			if (detalhes.get(i).equals(detalheEmFoco)) {
				return i;
			}
		}
		return null;
	}
}