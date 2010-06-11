package net.barragem.scaffold;

import java.text.ParseException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.barragem.persistence.entity.AtualizacaoTwitter;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
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

			JobDetail job = new JobDetail("job1", "group1", AtualizaTwitterJob.class);
			job.getJobDataMap().put("servletContext", e.getServletContext());

			Trigger trigger = new CronTrigger("trigger1", "group1", "0 0 0 ? * *");

			scheduler.scheduleJob(job, trigger);
			scheduler.start();

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