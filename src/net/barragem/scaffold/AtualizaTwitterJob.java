package net.barragem.scaffold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;

import net.barragem.persistence.entity.AtualizacaoTwitter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AtualizaTwitterJob implements Job {
	private ServletContext ctx;

	public AtualizaTwitterJob(ServletContext e) {
		this.ctx = e;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			String response = obtemUltimoTwitt();

			String twitt = extraiTwitt(response);
			Date twittDate = extraiTwittDate(response);

			AtualizacaoTwitter atualizacaoTwitter = (AtualizacaoTwitter) PersistenceHelper
					.findByNamedQuery("lastTwitterUpdateQuery");
			atualizacaoTwitter.setTexto(twitt);
			atualizacaoTwitter.setData(twittDate);
			atualizacaoTwitter.setDataGravacao(new Date());
			PersistenceHelper.persiste(atualizacaoTwitter);
			ctx.setAttribute("last-twitt", atualizacaoTwitter);
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
		String twitt = response.substring(beginIndex + "\"text\":\"".length(), response.length() - 5);
		return twitt;
	}

	private Date extraiTwittDate(String response) {
		try {
			int beginIndex = response.indexOf("\"created_at\":\"") + "\"created_at\":\"".length();
			int endIndex = response.indexOf("\"", beginIndex + 1);
			String data = response.substring(beginIndex, endIndex);
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", new java.util.Locale("eng",
					"US"));
			return sdf.parse(data);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}