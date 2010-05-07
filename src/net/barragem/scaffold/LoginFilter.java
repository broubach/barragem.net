package net.barragem.scaffold;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.business.bo.LoginBo;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String login = req.getParameter("login");
		String senha = req.getParameter("senha");
		if (login != null && !"".equals(login) && senha != null && !"".equals(senha)
				&& req.getSession().getAttribute("usuario") == null) {
			LoginBo loginBo = new LoginBo(req, resp);
			loginBo.autentica(login, senha);
		}

		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig filter) throws ServletException {
	}

}
