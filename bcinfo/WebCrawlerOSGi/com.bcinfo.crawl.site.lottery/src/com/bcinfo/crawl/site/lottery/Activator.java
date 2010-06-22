package com.bcinfo.crawl.site.lottery;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.crawl.dao.service.WebCrawlerDao;

public class Activator implements BundleActivator {

	private static Log log = LogFactory.getLog(Activator.class);
	
	private Scheduler scheduler;
	
	private long repeatInterval = 60 * 1000;
	
	public void start(BundleContext context) throws Exception {
		Properties config = new Properties();
		config.load(new FileInputStream(System.getProperty("user.dir") + "/conf/com.bcinfo.crawl.site.lottery.properties"));
		repeatInterval = Long.parseLong(config.getProperty("repeatInterval"));
		
		ServiceReference sr = context.getServiceReference(WebCrawlerDao.class.getName());
		WebCrawlerDao dao = (WebCrawlerDao)context.getService(sr);
		init(dao);
	}

	public void stop(BundleContext context) throws Exception {
		
		if(scheduler != null) {
			String name = scheduler.getSchedulerName();
			log.info(name + " is shutdown...");
			scheduler.shutdown(true);
			log.info(name + " is shutdown success...");
		}
		
	}

	public void init(WebCrawlerDao dao) throws Exception {
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(getClass().getResourceAsStream("quartz.properties"));
		scheduler = factory.getScheduler();
		scheduler.start();
		log.info(scheduler.getSchedulerName() + " is starting...");
		
		String name = "≤ ∆±";
		String group = "Õ¯¬Á≈¿≥Ê";
		JobDetail job = new JobDetail(name, group, LotteryJob.class);
		job.getJobDataMap().put("WebCrawlerDao", dao);
		Trigger trigger = new SimpleTrigger(name, group, new Date(), null, -1, repeatInterval);
		scheduler.scheduleJob(job, trigger);
		log.info(scheduler.getSchedulerName() + " job starting...");
	}
}
