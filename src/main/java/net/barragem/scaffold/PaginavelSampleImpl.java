package net.barragem.scaffold;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

public class PaginavelSampleImpl<T> implements Paginavel<T> {

	public static final int FIRST_PAGE = -1;
	public static final int LAST_PAGE = -2;

	private Integer maxPagesDisplayed = 10;
	private Integer pageSize = 10;
	private List<T> pagina = new ArrayList<T>();
	private Integer pageCount;
	private Integer paginaAtual = 1;
	private Integer totalRegistros;
	private PaginacaoListener<T> paginacaoListener;

	private List<T> sourceList;
	private String sourceHibernateNamedQuery;
	private String sourceHibernateCountNamedQuery;

	public PaginavelSampleImpl(String sourceHibernateNamedQuery, String sourceHibernateCountNamedQuery) {
		this.sourceHibernateNamedQuery = sourceHibernateNamedQuery;
		this.sourceHibernateCountNamedQuery = sourceHibernateCountNamedQuery;
	}

	public PaginavelSampleImpl(List<T> sourceList) {
		this(sourceList, null, 1, 10);
	}

	public PaginavelSampleImpl(List<T> sourceList, T focus) {
		this(sourceList, focus, 1, 10);
	}

	public PaginavelSampleImpl(List<T> sourceList, Integer paginaAtual, Integer pageSize) {
		this(sourceList, null, paginaAtual, pageSize);
	}

	public PaginavelSampleImpl(List<T> sourceList, T focus, Integer paginaAtual, Integer pageSize) {
		this.sourceList = sourceList;
		this.pageSize = pageSize != null ? pageSize : 10;
		int pageNumber = 1;
		if (focus != null) {
			pageNumber = calculaPagina(sourceList.indexOf(focus));
		} else if (paginaAtual != null && paginaAtual > 1) {
			pageNumber = paginaAtual <= calculaPagina(sourceList.size() - 1) ? paginaAtual : calculaPagina(sourceList
					.size() - 1);
		}
		pesquisaPaginavel(pageNumber);
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
		if (paginacaoListener != null) {
			paginacaoListener.afterPageChange(this);
		}
	}

	private String getIndex() {
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("index");
	}

	@Override
	public void pesquisaPaginavel(int pageNumber, Object... paramValues) {
		pagina.clear();
		if (sourceList == null) {
			totalRegistros = (Integer) PersistenceHelper.findByNamedQuery(sourceHibernateCountNamedQuery, paramValues)
					.get(0);
			pageCount = totalRegistros / pageSize;
			if (pageCount * pageSize < totalRegistros) {
				pageCount++;
			}
			if (pageNumber == FIRST_PAGE || pageNumber <= 0) {
				pageNumber = 1;
			} else if (pageNumber == LAST_PAGE || pageNumber > pageCount) {
				pageNumber = pageCount;
			}
			paginaAtual = pageNumber;

			int zeroBasedPageNumber = pageNumber - 1;
			pagina.addAll((List<T>) PersistenceHelper.findByNamedQueryWithLimits(sourceHibernateNamedQuery,
					zeroBasedPageNumber * pageSize, pageSize, paramValues));
		} else {
			totalRegistros = sourceList.size();
			pageCount = totalRegistros / pageSize;
			if (pageCount * pageSize < totalRegistros || pageCount == 0) {
				pageCount++;
			}
			if (pageNumber == FIRST_PAGE || pageNumber <= 0) {
				pageNumber = 1;
			} else if (pageNumber == LAST_PAGE || pageNumber > pageCount) {
				pageNumber = pageCount;
			}
			paginaAtual = pageNumber;

			int zeroBasedPageNumber = pageNumber - 1;
			pagina.addAll(sourceList.subList(zeroBasedPageNumber * pageSize,
					(zeroBasedPageNumber * pageSize + pageSize) > totalRegistros ? totalRegistros
							: (zeroBasedPageNumber * pageSize + pageSize)));
		}
	}

	public List<Integer> getPaginas() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = paginaAtual / maxPagesDisplayed; i < pageCount && i < (paginaAtual + maxPagesDisplayed); i++) {
			result.add(i + 1);
		}
		return result;
	}

	@Override
	public int calculaPagina(int zeroBasedPosicao) {
		return (zeroBasedPosicao / pageSize) + 1;
	}

	@Override
	public void setListener(PaginacaoListener<T> paginacaoListener) {
		this.paginacaoListener = paginacaoListener;
	}
}