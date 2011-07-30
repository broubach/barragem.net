package net.barragem.scaffold;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class RodadaMashupServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String barragemIdStr = req.getParameter("barragemId");
			String numRodadaStr = req.getParameter("numRodada");
			Integer barragemId = null;
			Integer numRodada = null;
			try {
				barragemId = Integer.parseInt(barragemIdStr);
				numRodada = Integer.parseInt(numRodadaStr);
			} catch (NumberFormatException e) {
				resp.getWriter().print("barragemId and numRodada must be passed as a valid number");
				return;
			}

			JSONObject rodadaEBonusJson = new JSONObject();
			JSONArray rodadaJson = new JSONArray();
			JSONArray bonusesJson = new JSONArray();

			List<Object[]> rodada = PersistenceHelper.findByNamedQuery("rodadaQuery", numRodada, barragemId);
			JSONObject jogoBarragemJson = null;
			Integer jogoId = null;
			Object[] jogadorVencedor = null;
			Object[] jogadorPerdedor = null;
			Object[] dataHoraEPlacar = null;
			SimpleDateFormat sdfData = new SimpleDateFormat("dd MMM", new Locale("PT", "br"));
			sdfData.setTimeZone(TimeZone.getTimeZone("GMT-3"));
			SimpleDateFormat sdfHora = new SimpleDateFormat(" HH:mm", new Locale("PT", "br"));
			sdfHora.setTimeZone(TimeZone.getTimeZone("GMT-3"));
			for (Object[] evento : rodada) {
				if (jogoId == null || !jogoId.equals(evento[0])) {
					if (jogoBarragemJson != null) {
						jogoBarragemJson.put("jogador1", jogadorVencedor[0]);
						jogoBarragemJson.put("pts1", jogadorVencedor[2] != null ? jogadorVencedor[2] : "");
						jogoBarragemJson.put("jogador2", jogadorPerdedor[0]);
						jogoBarragemJson.put("pts2", jogadorPerdedor[2] != null ? jogadorPerdedor[2] : "");
						jogoBarragemJson.put("dataHora", dataHoraEPlacar[0]);
						jogoBarragemJson.put("placar", dataHoraEPlacar[1]);

						rodadaJson.add(jogoBarragemJson);
					}
					jogoId = (Integer) evento[0];
					jogoBarragemJson = new JSONObject();
					jogadorVencedor = new Object[3];
					jogadorPerdedor = new Object[3];
					dataHoraEPlacar = new Object[2];
				}

				if (evento[2] != null && (Boolean) evento[2] || jogadorPerdedor[0] != null) {
					System.arraycopy(evento, 1, jogadorVencedor, 0, 3);
				} else {
					System.arraycopy(evento, 1, jogadorPerdedor, 0, 3);
				}

				dataHoraEPlacar[0] = (evento[4] != null && evento[5] != null) ? sdfData.format(evento[4])
						+ sdfHora.format(evento[5]) : "";
				if (evento[6] != null) {
					if (!PersistenceHelper.isInitialized(evento[6], "parciais")) {
						PersistenceHelper.initialize("parciais", evento[6]);
					}
					dataHoraEPlacar[1] = evento[6].toString();
				} else {
					dataHoraEPlacar[1] = "";
				}
			}

			List<Object[]> bonuses = PersistenceHelper.findByNamedQuery("bonusQuery", numRodada, barragemId);
			JSONObject bonusJson = null;
			for (Object[] bonus : bonuses) {
				bonusJson = new JSONObject();
				bonusJson.put("jogadorNome", bonus[0]);
				bonusJson.put("justificativa", bonus[1]);
				bonusJson.put("valor", bonus[2]);
				bonusesJson.add(bonusJson);
			}

			rodadaEBonusJson.put("jogos", rodadaJson);
			rodadaEBonusJson.put("bonus", bonusesJson);

			resp.setCharacterEncoding("UTF-8");
			if (req.getParameter("callback") != null) {
				resp.getWriter().print(req.getParameter("callback"));
			}
			resp.getWriter().print("(");
			resp.getWriter().print(rodadaEBonusJson);
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