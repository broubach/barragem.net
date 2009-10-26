package net.barragem.view.backingbean;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.barragem.persistence.entity.Usuario;
import net.barragem.util.MessageUtils;

import org.apache.commons.beanutils.BeanUtils;

public class BaseBean {

	private HtmlDataTable dataTable;

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	protected HttpServletRequest getServletRequest() {
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
	}

	public static void addErrorMessage(String clientId, String msgKey, String... parameters) {
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSummary(MessageUtils.getInstance().get(msgKey, parameters));
		facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage(clientId, facesMessage);
	}

	public static void addInfoMessage(String clientId, String msgKey, String... parameters) {
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSummary(MessageUtils.getInstance().get(msgKey, parameters));
		facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
		FacesContext.getCurrentInstance().addMessage(clientId, facesMessage);
	}

	public Boolean getExistemErros() {
		return FacesContext.getCurrentInstance().getMessages().hasNext();
	}

	public static void setApplicationAttribute(String key, Object value) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		request.getSession().getServletContext().setAttribute(key, value);
	}

	public void setSessionAttribute(String key, Object value) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		request.getSession().setAttribute(key, value);
	}

	public void setRequestAttribute(String key, Object value) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		request.setAttribute(key, value);
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	/**
	 * metodo que retorna um objeto do escopo de aplicacao
	 */
	public static Object getApplicationAttribute(String key) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return request.getSession().getServletContext().getAttribute(key);
	}

	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public void sendRedirect(String url) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		try {
			response.sendRedirect(request.getContextPath() + url);
		} catch (IOException e) {
			new RuntimeException(e);
		}
	}

	public HttpSession getSession() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request.getSession(true);
	}

	public Object getSessionAttribute(String atributo) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return request.getSession().getAttribute(atributo);
	}

	public Object getRequestAttribute(String atributo) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return request.getAttribute(atributo);
	}

	public void removeSessionAttribute(String key) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		request.getSession().removeAttribute(key);
	}

	/**
	 * Forca renderizacao de pagina. Se usuario alterar conteudo da aba e
	 * selecionar esta mesma aba, renderizacao deve ser forcada.
	 */
	protected void refreshView() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();
	}

	public String getUrl() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
	}

	public String getContextPath() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}

	protected Usuario getUsuarioLogado() {
		return (Usuario) getSessionAttribute("usuario");
	}

	protected void setUsuarioLogado(Usuario usuario) {
		setSessionAttribute("usuario", usuario);
	}

	protected int getIndex() {
		return Integer.valueOf(getDataTable().getRowIndex());
	}

	protected int getId() {
		String id = getServletRequest().getParameter("id");
		if (id == null || "".equals(id)) {
			id = (String) getRequestAttribute("id");
		}
		return Integer.parseInt(id);
	}

	protected Object getById(Collection<? extends Object> collection, int id) {
		try {
			for (Iterator<? extends Object> it = collection.iterator(); it.hasNext();) {
				Object o = it.next();
				int idFound;
				idFound = Integer.valueOf(BeanUtils.getSimpleProperty(o, "id"));

				if (idFound == id) {
					return o;
				}
			}
			return null;
		} catch (NumberFormatException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}