package net.barragem.scaffold;

import java.util.List;

public interface Paginavel<T> {

	public int getPaginaAtual();

	public void setPaginaAtual(int pagina);

	public int getPageSize();

	public void setPageSize(int pageSize);

	public int getPageCount();

	public void setPageCount(int pageCount);

	public int getMaxPagesDisplayed();

	public void setMaxPagesDisplayed(int maxPagesDisplayed);

	public List<T> getPagina();

	public void setPagina(List<T> pagina);

	public String getSourceHibernateNamedQuery();

	public String getSourceHibernateCountNamedQuery();

	public List<T> getSourceList();

	public void pesquisaPaginavel(int pageNumber, Object... paramValues);

	public int calculaPagina(int indexOf);

	public T getPosteriorImediatoOuAnteriorImediato(int indexInPage);
}
