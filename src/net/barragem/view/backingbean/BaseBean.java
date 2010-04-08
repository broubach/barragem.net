package net.barragem.view.backingbean;

import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Conta;
import net.barragem.persistence.entity.Usuario;
import net.barragem.persistence.entity.Validatable;
import net.barragem.scaffold.BarragemJmsTemplate;
import net.barragem.scaffold.PersistenceHelper;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class BaseBean {

	public static final String FOTO_DEFAULT_JOGADOR_KEY = "foto-jogador";
	public static final Integer FOTO_DEFAULT_JOGADOR_ID = new Integer(2);

	private HtmlDataTable dataTable;

	protected final MessageBean messages = new MessageBean();

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
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

	public Usuario getUsuarioLogado() {
		return (Usuario) getSessionAttribute("usuario");
	}

	protected void setUsuarioLogado(Usuario usuario) {
		setSessionAttribute("usuario", usuario);
	}

	protected int getIndex() {
		String index = getRequest().getParameter("index");
		if (index == null || "".equals(index)) {
			index = (String) getRequestAttribute("index");
			if (index == null || "".equals(index)) {
				return Integer.valueOf(getDataTable().getRowIndex());
			}
		}
		return Integer.parseInt(index);

	}

	protected int getId() {
		String id = getRequest().getParameter("id");
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

	protected void addMensagemAtualizacaoComSucesso() {
		messages.addInfoMessage(null, "label_informacao_atualizada_com_sucesso");
	}

	protected boolean valida(Validatable validatable) {
		for (String clientId : validatable.validate()) {
			messages.addErrorMessage(clientId, "label_true");
		}

		return messages.getErrorMessages().isEmpty();
	}

	protected String encriptMd5(String senha) {
		byte[] defaultBytes = senha.getBytes();
		MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	protected String fillLeft(String toBeFilled, int size, char fillChar) {
		char[] fill = new char[size];
		Arrays.fill(fill, fillChar);

		char[] toBeFilledArray = toBeFilled.toCharArray();
		for (int i = 0; i < toBeFilledArray.length; i++) {
			fill[size - (toBeFilledArray.length - i)] = toBeFilledArray[i];
		}
		return String.valueOf(fill);
	}

	protected void sendMail(final String from, final String to, final String subject, final String body) {
		BarragemJmsTemplate template = (BarragemJmsTemplate) WebApplicationContextUtils.getWebApplicationContext(
				getRequest().getSession().getServletContext()).getBean("jmsTemplate");
		if (template.getEnabled()) {
			template.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					MapMessage message = session.createMapMessage();
					message.setString("from", from);
					message.setString("to", to);
					message.setString("subject", subject);
					message.setString("body", body);
					return message;
				}
			});
		}
	}

	protected ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	public Arquivo getFotoDefaultJogador() {
		return (Arquivo) getServletContext().getAttribute(FOTO_DEFAULT_JOGADOR_KEY);
	}

	protected Object evaluateElExpression(String elExpression) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ValueExpression expression = app.getExpressionFactory().createValueExpression(context.getELContext(),
				elExpression, Object.class);
		return expression.getValue(context.getELContext());
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	protected UIComponent findComponent(UIComponent c, String id) {
		if (id.equals(c.getId())) {
			return c;
		}
		Iterator<UIComponent> kids = c.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = findComponent(kids.next(), id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public List<Barragem> getBarragens() {
		return getBarragens(getUsuarioLogado().getId());
	}

	protected List<Barragem> getBarragens(Integer usuarioId) {
		return (List<Barragem>) PersistenceHelper.findByNamedQuery("barragensDeUsuarioQuery", usuarioId);
	}

	protected void setContaUsuario(Conta conta) {
		setSessionAttribute("conta", conta);
	}

	protected Conta getContaUsuario() {
		return (Conta) getSessionAttribute("conta");
	}

	protected boolean validaSenha(String clientId, String senha) {
		if (senha.length() < 6) {
			messages.addErrorMessage(clientId, "error_a_senha_deve_conter_no_minimo_6_caracteres");
			return false;
		}
		return true;
	}

	protected <T> T getBo(Class<T> bo) {
		try {
			return bo.getConstructor(HttpServletRequest.class, HttpServletResponse.class).newInstance(getRequest(),
					getResponse());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	protected void setAtualizacoes(List<Atualizacao> atualizacoes) {
		setSessionAttribute("atualizacoes", atualizacoes);
	}

	protected List<Atualizacao> getAtualizacoes() {
		return (List<Atualizacao>) getSessionAttribute("atualizacoes");
	}
}