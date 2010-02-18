package net.barragem.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.view.backingbean.BaseBean;

public class ImageServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Arquivo fotoDefault = (Arquivo) getServletContext().getAttribute(BaseBean.FOTO_DEFAULT_JOGADOR_KEY);
		String hash = req.getParameter("hash");
		if (hash.equals("default")) {
			resp.setContentType(fotoDefault.getMime());
			resp.getOutputStream().write(fotoDefault.getDado());
		} else {
			List<Integer> fotoIds = PersistenceHelper.findByNamedQuery("fotoIdQueryByHash", hash);
			if (fotoIds.size() > 0) {
				if (fotoIds.get(0).equals(fotoDefault.getId())) {
					resp.setContentType(fotoDefault.getMime());
					resp.getOutputStream().write(fotoDefault.getDado());
				} else {
					Arquivo arquivo = PersistenceHelper.findByPk(Arquivo.class, fotoIds.get(0));
					resp.setContentType(arquivo.getMime());
					resp.getOutputStream().write(arquivo.getDado());
				}
			}
		}
	}
}
