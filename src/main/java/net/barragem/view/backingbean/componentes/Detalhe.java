package net.barragem.view.backingbean.componentes;

import java.util.List;

public interface Detalhe<T> {

	public List<T> getDetalhes();

	public void setDetalhes(List<T> detalhes);

	public T getDetalheEmFoco();

	public void setDetalheEmFoco(T detalhe);

	public Integer getIndiceNosDetalhes();
}
