package net.barragem.scaffold;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class RodadasMashupServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String barragemIdStr = req.getParameter("barragemId");
			Integer barragemId = null;
			try {
				barragemId = Integer.parseInt(barragemIdStr);
			} catch (NumberFormatException e) {
				resp.getWriter().print("barragemId must be passed as a valid number");
				return;
			}

			List<Object[]> barragem = PersistenceHelper.findByNamedQuery("cicloNomeEBarragemComRodadasQuery",
					barragemId);

			JSONObject barragemJson = new JSONObject();
			if (barragem.size() > 0) {
				barragemJson.put("id", barragem.get(0)[0]);
				barragemJson.put("local", barragem.get(0)[3]);
				barragemJson.put("categoria", MessageBundleUtils.getInstance().get((String) barragem.get(0)[4]));
				barragemJson.put("ciclo", barragem.get(0)[1] != null ? barragem.get(0)[1] : barragem.get(0)[2]);
				barragemJson.put("rodadas", barragem.get(0)[5]);
			}

			resp.setCharacterEncoding("UTF-8");
			if (req.getParameter("callback") != null) {
				resp.getWriter().print(req.getParameter("callback"));
			}
			resp.getWriter().print("(");
			resp.getWriter().print(barragemJson);
			resp.getWriter().print(");");

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}