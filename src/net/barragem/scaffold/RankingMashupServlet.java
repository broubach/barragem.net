package net.barragem.scaffold;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;

public class RankingMashupServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String barragemIdStr = req.getParameter("barragemId");
			Integer barragemId = Integer.parseInt(barragemIdStr);

			List<Object[]> result = PersistenceHelper.findByNamedQuery("cicloNomeEBarragemQuery", barragemId);
			Object[] cicloNomeEBarragem = result.get(0);

			JSONObject barragemJson = new JSONObject();
			barragemJson.put("local", cicloNomeEBarragem[2]);
			barragemJson.put("categoria", MessageBundleUtils.getInstance().get((String) cicloNomeEBarragem[3]));
			barragemJson.put("ciclo", cicloNomeEBarragem[0] != null ? cicloNomeEBarragem[0] : cicloNomeEBarragem[1]);

			List<Object[]> ranking = PersistenceHelper.findByNamedQuery("rankingPorBarragemIdQuery", barragemId);

			JSONArray rankingJson = new JSONArray();
			JSONObject cicloJogadorJson = null;
			for (Object[] cicloJogador : ranking) {
				cicloJogadorJson = new JSONObject();
				cicloJogadorJson.put("nome", cicloJogador[0]);
				cicloJogadorJson.put("ranking", cicloJogador[1]);
				cicloJogadorJson.put("pontuacao", cicloJogador[2]);
				rankingJson.put(cicloJogadorJson);
			}
			barragemJson.put("ranking", rankingJson);
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