package net.barragem.scaffold;

import java.util.List;

public class PaginavelSampleImpl<T> implements Paginavel<T> {

	private int maxPagesDisplayed = 10;
	private int pageSize = 10;
	private List<T> pagina;
	private int pageCount;
	private int paginaAtual = 0;

	private List<T> sourceList;
	private String sourceHibernateNamedQuery;
	private String sourceHibernateCountNamedQuery;

	public PaginavelSampleImpl(String sourceHibernateNamedQuery, String sourceHibernateCountNamedQuery) {
		this.sourceHibernateNamedQuery = sourceHibernateNamedQuery;
		this.sourceHibernateCountNamedQuery = sourceHibernateCountNamedQuery;
	}

	public PaginavelSampleImpl(List<T> sourceList) {
		this.sourceList = sourceList;
	}

	@Override
	public int getPageCount() {
		return pageCount;
	}

	@Override
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public void setMaxPagesDisplayed(int maxPagesDisplayed) {
		this.maxPagesDisplayed = maxPagesDisplayed;
	}

	@Override
	public int getMaxPagesDisplayed() {
		return maxPagesDisplayed;
	}

	@Override
	public void setPaginaAtual(int paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	@Override
	public int getPaginaAtual() {
		return paginaAtual;
	}

	@Override
	public List<T> getPagina() {
		return pagina;
	}

	@Override
	public void setPagina(List<T> pagina) {
		this.pagina = pagina;
	}

	@Override
	public String getSourceHibernateCountNamedQuery() {
		return sourceHibernateCountNamedQuery;
	}

	@Override
	public String getSourceHibernateNamedQuery() {
		return sourceHibernateNamedQuery;
	}

	@Override
	public List<T> getSourceList() {
		return sourceList;
	}

	@Override
	public void pesquisaPaginavel(int pageNumber, Object[] paramValues) {
		if (sourceList == null) {
			pagina = (List<T>) PersistenceHelper.findByNamedQueryWithLimits(sourceHibernateNamedQuery, pageNumber
					* pageSize, pageSize, paramValues);
			paginaAtual = pageNumber;
			int totalRegistros = (Integer) PersistenceHelper.findByNamedQuery(sourceHibernateCountNamedQuery,
					paramValues).get(0);
			pageCount = totalRegistros / pageSize;
			if (pageCount * pageSize < totalRegistros) {
				pageCount++;
			}
		} else {
			pagina = sourceList.subList(pageNumber * pageSize,
					(pageNumber * pageSize + pageSize) > sourceList.size() ? sourceList.size()
							: (pageNumber * pageSize + pageSize));
			paginaAtual = pageNumber;
			pageCount = sourceList.size() / pageSize;
			if (pageCount * pageSize < sourceList.size()) {
				pageCount++;
			}
		}
	}
}