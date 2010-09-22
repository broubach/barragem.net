package net.barragem.view.backingbean;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
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
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.scaffold.ReflectionHelper;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class BaseBean {

	public static final String FOTO_DEFAULT_JOGADOR_KEY = "foto-jogador";
	public static final String FOTO_PEQUENA_DEFAULT_JOGADOR_KEY = "foto-pequena-jogador";

	public static final Integer FOTO_DEFAULT_JOGADOR_ID = new Integer(2);
	public static final Integer FOTO_PEQUENA_DEFAULT_JOGADOR_ID = new Integer(64);

	public static final int FOTO_PEQUENA_HEIGHT = 49;
	public static final int FOTO_PEQUENA_WIDTH = 37;
	public static final int FOTO_DEFAULT_HEIGHT = 162;
	public static final int FOTO_DEFAULT_WIDTH = 123;

	protected static final String emailTemplateAdicaoJogador = new StringBuilder().append("<p>Olá,</p>").append(
			"<p>Agora você faz parte da lista de jogadores de {0}.</p>").append(
			"<p>Se você conhece {0}, você também pode adicioná-lo(a) à sua lista de jogadores.</p>").append(
			"<p>Atenciosamente,<br />Equipe <a href='https://www.barragem.net/login.xhtml'>barragem.net</a></p>")
			.toString();
	protected static final String emailNovaMensagem = new StringBuilder()
			.append("<p>Olá,</p>")
			.append("<p>Você recebeu uma mensagem de {0}.</p>")
			.append(
					"<p>Para ler o seu conteúdo, acesse <a href='https://www.barragem.net/login.xhtml'>barragem.net</a>.</p>")
			.append(
					"<p>Atenciosamente,<br />Equipe <a href='https://www.barragem.net/login.xhtml'>barragem.net</a></p>")
			.toString();
	protected static final String emailTemplateRecuperarSenha = new StringBuilder()
			.append("<p>Olá,</p>")
			.append(
					"<p>Visite <a href=\"https://www.barragem.net/publicpages/recuperarsenha/recuperarSenha.xhtml?hash={0}\">esta página</a> para recuperar a sua senha. Esta página será válida por 2 dias.</p>")
			.append(
					"<p>Atenciosamente,<br />Equipe <a href='https://www.barragem.net/login.xhtml'>barragem.net</a></p>")
			.toString();
	protected static final String emailTemplateSorteioBarragem = new StringBuilder()
			.append("<p>Olá,</p>")
			.append("<p>O sorteio da barragem {0} foi realizado.</p>")
			.append(
					"<p>Acesse <a href='https://www.barragem.net/login.xhtml'>www.barragem.net</a> e confira quem será o seu próximo(a) adversário(a).</p>")
			.append(
					"<p>Atenciosamente,<br />Equipe <a href='https://www.barragem.net/login.xhtml'>barragem.net</a></p>")
			.toString();
	protected static final String emailTemplateNovoJogo = new StringBuilder()
			.append("<p>Olá,</p>")
			.append("<p>Um novo jogo entre você e {0} foi cadastrado.</p>")
			.append(
					"<p>Acesse <a href='https://www.barragem.net/login.xhtml'>www.barragem.net</a> e confira outros dados do evento.</p>")
			.append(
					"<p>Atenciosamente,<br />Equipe <a href='https://www.barragem.net/login.xhtml'>barragem.net</a></p>")
			.toString();

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

	protected void sendMail(final String from, final String fromName, final String to, final String subject,
			final String body) {
		BarragemJmsTemplate template = (BarragemJmsTemplate) WebApplicationContextUtils.getWebApplicationContext(
				getRequest().getSession().getServletContext()).getBean("jmsTemplate");
		if (template.getEnabled()) {
			template.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					MapMessage message = session.createMapMessage();
					message.setString("from", from);
					message.setString("fromName", fromName);
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

	public Arquivo getFotoPequenaJogador() {
		return (Arquivo) getServletContext().getAttribute(FOTO_PEQUENA_DEFAULT_JOGADOR_KEY);
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

	protected void setAtualizacoes(Paginavel<Atualizacao> paginacaoAtualizacoes) {
		setSessionAttribute("atualizacoes", paginacaoAtualizacoes);
	}

	protected Paginavel<Atualizacao> getAtualizacoes() {
		return (Paginavel<Atualizacao>) getSessionAttribute("atualizacoes");
	}

	public String executaMetodo() {
		String metodo = (String) getRequest().getParameter("metodo");
		return (String) ReflectionHelper.execute(this, metodo);
	}

	protected void setTotalNovasMensagens(Integer totalNovasMensagens) {
		setSessionAttribute("totalNovasMensagens", totalNovasMensagens);
	}

	protected boolean updateModelValues(FacesEvent evt) {
		PhaseId phaseId = evt.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			evt.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			evt.queue();
			return true;
		}
		return false;
	}

	public static byte[] scaleImage(byte[] original, int width, int height) throws IOException {
		BufferedImage bsrc = ImageIO.read(new ByteArrayInputStream(original));
		BufferedImage bdest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		AffineTransform at = AffineTransform.getScaleInstance((double) width / bsrc.getWidth(), (double) height
				/ bsrc.getHeight());
		Graphics2D g = bdest.createGraphics();
		g.drawRenderedImage(bsrc, at);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(bdest, "PNG", out);
		return out.toByteArray();
	}

	public byte[] cropAndScaleImage(byte[] original, int x1, int y1, int x2, int y2, int width, int height)
			throws IOException {
		BufferedImage bsrc = ImageIO.read(new ByteArrayInputStream(original));
		BufferedImage bdest = null;

		if (x2 - x1 > 0 && y2 - y1 > 0) {
			bdest = getScaledInstance(bsrc.getSubimage(x1, y1, x2 - x1, y2 - y1), width, height,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
		} else {
			bdest = getScaledInstance(bsrc, width, height, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(bdest, "PNG", out);

		return out.toByteArray();
	}

	public String getRequestParameter(String parametro) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return request.getParameter(parametro);
	}

	/**
	 * Convenience method that returns a scaled instance of the provided {@code
	 * BufferedImage}.
	 * 
	 * @param img
	 *            the original image to be scaled
	 * @param targetWidth
	 *            the desired width of the scaled instance, in pixels
	 * @param targetHeight
	 *            the desired height of the scaled instance, in pixels
	 * @param hint
	 *            one of the rendering hints that corresponds to {@code
	 *            RenderingHints.KEY_INTERPOLATION} (e.g. {@code
	 *            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR}, {@code
	 *            RenderingHints.VALUE_INTERPOLATION_BILINEAR}, {@code
	 *            RenderingHints.VALUE_INTERPOLATION_BICUBIC})
	 * @param higherQuality
	 *            if true, this method will use a multi-step scaling technique
	 *            that provides higher quality than the usual one-step technique
	 *            (only useful in downscaling cases, where {@code targetWidth}
	 *            or {@code targetHeight} is smaller than the original
	 *            dimensions, and generally only when the {@code BILINEAR} hint
	 *            is specified)
	 * @return a scaled version of the original {@code BufferedImage}
	 */
	public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint,
			boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			} else if (w <= targetWidth) {
				w = targetWidth;
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			} else if (h <= targetHeight) {
				h = targetHeight;
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}
}