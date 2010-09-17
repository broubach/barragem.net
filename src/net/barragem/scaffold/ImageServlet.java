package net.barragem.scaffold;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
		Arquivo fotoPequenaDefault = (Arquivo) getServletContext().getAttribute(
				BaseBean.FOTO_PEQUENA_DEFAULT_JOGADOR_KEY);
		String hash = req.getParameter("hash");
		boolean isFotoPequena = req.getParameter("fotoPequena") != null;
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-3"));
		calendar.add(Calendar.YEAR, 1);
		resp.setHeader("Expires", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).format(calendar
				.getTime()));
		resp.setHeader("ETag", hash + isFotoPequena);
		if (hash.equals("default")) {
			if (isFotoPequena) {
				resp.setContentType(fotoPequenaDefault.getMime());
				resp.getOutputStream().write(fotoPequenaDefault.getDado());
			} else {
				resp.setContentType(fotoDefault.getMime());
				resp.getOutputStream().write(fotoDefault.getDado());
			}
		} else {
			List<Integer> fotoIds = null;
			if (isFotoPequena) {
				fotoIds = PersistenceHelper.findByNamedQuery("fotoPequenaIdQueryByHash", hash);
			} else {
				fotoIds = PersistenceHelper.findByNamedQuery("fotoIdQueryByHash", hash);
			}
			if (fotoIds.size() > 0) {
				if (fotoIds.get(0).equals(fotoDefault.getId())) {
					resp.setContentType(fotoDefault.getMime());
					resp.getOutputStream().write(fotoDefault.getDado());

				} else if (fotoIds.get(0).equals(fotoPequenaDefault.getId())) {
					resp.setContentType(fotoPequenaDefault.getMime());
					resp.getOutputStream().write(fotoPequenaDefault.getDado());

				} else {
					Arquivo arquivo = PersistenceHelper.findByPk(Arquivo.class, fotoIds.get(0));
					resp.setContentType(arquivo.getMime());
					resp.getOutputStream().write(arquivo.getDado());
				}
			}
		}
	}
}
