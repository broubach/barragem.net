package net.barragem.scaffold;

import java.text.ParseException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class BarragemServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent e) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            e.getServletContext().setAttribute("quartz-scheduler", scheduler);

            instalaJobTwitter(e.getServletContext(), scheduler);
            instalaJobUltimosEventos(e.getServletContext(), scheduler);

            scheduler.start();

        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void instalaJobUltimosEventos(ServletContext ctx, Scheduler scheduler) throws ParseException,
            SchedulerException {
        JobDetail jobUltimosEventos = new JobDetail("jobUltimosEventos", "group1", UltimosEventosJob.class);
        jobUltimosEventos.getJobDataMap().put("servletContext", ctx);
        CronTrigger ultimosEventosTrigger = new CronTrigger("trigger1", "group1", "jobUltimosEventos", "group1",
                "0 0/10 * * * ?");
        scheduler.scheduleJob(jobUltimosEventos, ultimosEventosTrigger);

        UltimosEventosJob.doExecute(ctx);
    }

    private void instalaJobTwitter(ServletContext ctx, Scheduler scheduler) throws ParseException, SchedulerException {
        JobDetail jobTwitter = new JobDetail("jobTwitter", "group1", AtualizaTwitterJob.class);
        jobTwitter.getJobDataMap().put("servletContext", ctx);
        CronTrigger twitterTrigger = new CronTrigger("trigger2", "group1", "jobTwitter", "group1", "0 0 0/12 * * ?");

        scheduler.scheduleJob(jobTwitter, twitterTrigger);

        AtualizaTwitterJob.doExecute(ctx);
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