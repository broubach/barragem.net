package net.barragem.scaffold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import net.barragem.persistence.entity.AtualizacaoTwitter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AtualizaTwitterJob implements Job {

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		try {
			String response = obtemUltimoTwitt();

			String twitt = extraiTwitt(response);
			List<Date> twittDates = new ArrayList<Date>();
			extraiTwittDate(twittDates, response, 0);
			Date twittDateMaisRecente = twittDates.get(0);
			for (Date twittDate : twittDates) {
				if (twittDateMaisRecente.before(twittDate)) {
					twittDateMaisRecente = twittDate;
				}
			}

			AtualizacaoTwitter atualizacaoTwitter = (AtualizacaoTwitter) PersistenceHelper.findByNamedQuery(
					"lastTwitterUpdateQuery").get(0);
			atualizacaoTwitter.setTexto(twitt);
			atualizacaoTwitter.setData(twittDateMaisRecente);
			atualizacaoTwitter.setDataGravacao(new Date());
			PersistenceHelper.persiste(atualizacaoTwitter);
			((ServletContext) ctx.getMergedJobDataMap().get("servletContext")).setAttribute("last-twitt",
					atualizacaoTwitter);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String obtemUltimoTwitt() throws MalformedURLException, IOException {
		URL lastTwitt = new URL(
				"http://twitter.com/statuses/user_timeline/barragemnet.json?callback=twitterCallback&count=1");

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(lastTwitt.openStream()));

			StringBuffer response = new StringBuffer();
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			return response.toString();
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	private String extraiTwitt(String response) {
		int beginIndex = response.indexOf("\"text\":\"");
		String twitt = response.substring(beginIndex + "\"text\":\"".length(), response.indexOf("\"", beginIndex
				+ "\"text\":\"".length()));
		return twitt;
	}

	private void extraiTwittDate(List<Date> twittDates, String response, int beginIndex) {
		try {
			if (beginIndex == 0) {
				beginIndex = response.indexOf("\"created_at\":\"") + "\"created_at\":\"".length();
			} else {
				beginIndex = response.indexOf("\"created_at\":\"", beginIndex) >= 0 ? response.indexOf(
						"\"created_at\":\"", beginIndex)
						+ "\"created_at\":\"".length() : -1;
			}
			if (beginIndex < 0) { // condicao de parada
				return;
			}
			int endIndex = response.indexOf("\"", beginIndex + 1);
			String data = response.substring(beginIndex, endIndex);
			extraiTwittDate(twittDates, response, endIndex);
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", new java.util.Locale("eng",
					"US"));
			twittDates.add(sdf.parse(data));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}