package com.bcinfo.crawl.channel.movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.crawl.channel.movie.service.SiteParser;
import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Site;
import com.bcinfo.crawl.utils.SiteUtil;

public class Activator implements BundleActivator {

	private static final Log log = LogFactory.getLog(Activator.class);
	
	private Scheduler scheduler = null;
	
	public List<Site> getSites() throws Exception {
		List<Site> sites = new ArrayList<Site>();
		String configPath = "conf/com.bcinfo.crawl.channel.movie.xml";
		sites.addAll(new SiteUtil().getSites(configPath));
		return sites;
	}
	
	public void start(BundleContext context) throws Exception {
		log.info("电影频道抓取启动");
		CrawlerLogService crawlerLogService = (CrawlerLogService)context.getService(context.getServiceReference(CrawlerLogService.class.getName()));
		WebCrawlerDao webCrawlerDao = (WebCrawlerDao)context.getService(context.getServiceReference(WebCrawlerDao.class.getName()));
		
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
		int index = 1;
		for(Site site : getSites()) {
			String name = site.getChannelId()+"."+site.getChannelName()+"."+index;
			String group = "电影";
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
		log.info("电影频道抓取关闭");
	}

}
