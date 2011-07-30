package net.barragem.scaffold;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.view.backingbean.ArquivoBean;

public class FotoDefaultJogadorFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		ServletContext ctx = ((HttpServletRequest) req).getSession().getServletContext();
		if (ctx.getAttribute(ArquivoBean.FOTO_DEFAULT_JOGADOR_KEY) == null) {
			ctx.setAttribute(ArquivoBean.FOTO_DEFAULT_JOGADOR_KEY, PersistenceHelper.findByPk(Arquivo.class,
					ArquivoBean.FOTO_DEFAULT_JOGADOR_ID));
		}
		if (ctx.getAttribute(ArquivoBean.FOTO_PEQUENA_DEFAULT_JOGADOR_KEY) == null) {
			ctx.setAttribute(ArquivoBean.FOTO_PEQUENA_DEFAULT_JOGADOR_KEY, PersistenceHelper.findByPk(Arquivo.class,
					ArquivoBean.FOTO_PEQUENA_DEFAULT_JOGADOR_ID));
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}