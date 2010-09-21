package net.barragem.scaffold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
			response = response.replace("twitterCallback([", "");
			response = response.replace("]);", "");

			Json2Java j = new Json2Java();
			Map<String, Object> resp = j.getMap(response);

			String twitt = (String) resp.get("text");
			Date twittDate = toDate((String) resp.get("created_at"));

			AtualizacaoTwitter atualizacaoTwitter = (AtualizacaoTwitter) PersistenceHelper.findByNamedQuery(
					"lastTwitterUpdateQuery").get(0);
			atualizacaoTwitter.setTexto(twitt);
			atualizacaoTwitter.setData(twittDate);
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

	private Date toDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", new java.util.Locale("eng",
					"US"));
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}