package net.barragem.scaffold;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

public class PaginavelSampleImpl<T> implements Paginavel<T> {

	private int maxPagesDisplayed = 10;
	private int pageSize = 10;
	private List<T> pagina = new ArrayList<T>();
	private int pageCount;
	private int paginaAtual = 1;

	private List<T> sourceList;
	private String sourceHibernateNamedQuery;
	private String sourceHibernateCountNamedQuery;

	public PaginavelSampleImpl(String sourceHibernateNamedQuery, String sourceHibernateCountNamedQuery) {
		this.sourceHibernateNamedQuery = sourceHibernateNamedQuery;
		this.sourceHibernateCountNamedQuery = sourceHibernateCountNamedQuery;
	}

	public PaginavelSampleImpl(List<T> sourceList) {
		this.sourceList = sourceList;
		pesquisaPaginavel(1);
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

	public void pagina(ActionEvent e) {
		if (Integer.parseInt(getIndex()) <= 0 || Integer.parseInt(getIndex()) > pageCount) {
			return;
		}
		pesquisaPaginavel(Integer.parseInt(getIndex()));
	}

	private String getIndex() {
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("index");
	}

	@Override
	public void pesquisaPaginavel(int pageNumber, Object... paramValues) {
		pagina.clear();
		int zeroBasedPageNumber = pageNumber - 1;
		if (sourceList == null) {
			pagina.addAll((List<T>) PersistenceHelper.findByNamedQueryWithLimits(sourceHibernateNamedQuery,
					zeroBasedPageNumber * pageSize, pageSize, paramValues));
			paginaAtual = pageNumber;
			int totalRegistros = (Integer) PersistenceHelper.findByNamedQuery(sourceHibernateCountNamedQuery,
					paramValues).get(0);
			pageCount = totalRegistros / pageSize;
			if (pageCount * pageSize < totalRegistros) {
				pageCount++;
			}
		} else {
			pagina.addAll(sourceList.subList(zeroBasedPageNumber * pageSize,
					(zeroBasedPageNumber * pageSize + pageSize) > sourceList.size() ? sourceList.size()
							: (zeroBasedPageNumber * pageSize + pageSize)));
			paginaAtual = pageNumber;
			pageCount = sourceList.size() / pageSize;
			if (pageCount * pageSize < sourceList.size()) {
				pageCount++;
			}
		}
	}

	public List<Integer> getPaginas() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = paginaAtual / maxPagesDisplayed; i < pageCount && i < (paginaAtual + maxPagesDisplayed); i++) {
			result.add(i + 1);
		}
		return result;
	}
}