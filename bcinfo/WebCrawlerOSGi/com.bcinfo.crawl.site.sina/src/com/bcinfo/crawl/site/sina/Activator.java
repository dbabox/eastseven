package com.bcinfo.crawl.site.sina;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Site;
import com.bcinfo.crawl.utils.SiteUtil;

public class Activator implements BundleActivator {

	private static Log log = LogFactory.getLog(Activator.class);
	
	private Scheduler scheduler;
	
	public void start(BundleContext context) throws Exception {
		log.info("站点-"+Site.SINA+"-抓取程序启动");
		CrawlerLogService crawlerLogService = (CrawlerLogService)context.getService(context.getServiceReference(CrawlerLogService.class.getName()));
		WebCrawlerDao webCrawlerDao = (WebCrawlerDao)context.getService(context.getServiceReference(WebCrawlerDao.class.getName()));
		
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
		int index = 1;
		for(Site site : new SiteUtil().getSites("conf/com.bcinfo.crawl.site.sina.xml")) {
			String name = site.getChannelId()+"."+site.getChannelName()+"."+index;
			String group = site.getName();
			JobDetail job = new JobDetail(name, group, SiteParser.class);
			job.getJobDataMap().put("site", site);
			job.getJobDataMap().put("webCrawlerDao", webCrawlerDao);
			job.getJobDataMap().put("crawlerLogService", crawlerLogService);
			Trigger trigger = new SimpleTrigger(name, group, new Date(System.currentTimeMillis()+index*1000), null, -1, site.getFrequency());
			scheduler.scheduleJob(job, trigger);
			index++;
		}
	}

	public void stop(BundleContext context) throws Exception {
		if(scheduler != null) scheduler.shutdown(true);
		log.info("站点-"+Site.SINA+"-抓取程序关闭");
	}

}
