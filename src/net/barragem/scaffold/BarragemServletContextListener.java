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
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.barragem.persistence.entity.AtualizacaoTwitter;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class BarragemServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(final ServletContextEvent e) {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			e.getServletContext().setAttribute("quartz-scheduler", scheduler);

			scheduler.start();

			JobDetail job = new JobDetail("job1", "group1", new AtualizaTwitterJob(e.getServletContext()).getClass());

			// disparar Job todos os dias a 00h
			Trigger trigger = new CronTrigger("trigger1", "group1", "0 0 0 ? * *");

			scheduler.scheduleJob(job, trigger);

			AtualizacaoTwitter atualizacaoTwitter = (AtualizacaoTwitter) PersistenceHelper.findByNamedQuery(
					"lastTwitterUpdateQuery").get(0);
			e.getServletContext().setAttribute("last-twitt", atualizacaoTwitter);

		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		} catch (SchedulerException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
		try {
			Scheduler scheduler = (Scheduler) e.getServletContext().getAttribute("quartz-scheduler");

			scheduler.shutdown();
		} catch (SchedulerException ex) {
			throw new RuntimeException(ex);
		}
	}
}

class AtualizaTwitterJob implements Job {
	private final ServletContext ctx;

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