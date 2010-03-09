package net.barragem.scaffold;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RecuperarSenhaProtectionFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, -2);
		String hash = req.getParameter("hash") == null ? req.getParameter("recuperarSenhaForm:hash") : req
				.getParameter("hash");
		if (hash != null) {
			req.setAttribute("recuperarSenhaForm:hash", hash);
		}
		if (hash == null
				|| PersistenceHelper.findByNamedQuery("findRequisicaoValidaByHashQuery", calendar.getTime(), hash)
						.size() <= 0) {
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/login.xhtml"));
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
