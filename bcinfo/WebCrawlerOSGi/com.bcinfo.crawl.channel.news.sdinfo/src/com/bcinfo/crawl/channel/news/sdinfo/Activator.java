package com.bcinfo.crawl.channel.news.sdinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Site;
import com.bcinfo.crawl.utils.SiteUtil;

public class Activator implements BundleActivator {

	private static final Log log = LogFactory.getLog(Activator.class);

	private ScheduledExecutorService scheduled = null;

	public List<Site> getSites() {
		List<Site> sites = new ArrayList<Site>();
		/*
		Site site = null;
		
		site = new Site();
		site.setChannelId(981L);
		site.setChannelName("山东新闻");
		site.setUrl("http://news.sdinfo.net/sdxw/sdyw/default.shtml");
		site.setPageSuffix("sdyw/\\d+.shtml");
		site.setDatePattern("yyyy-MM-dd");
		site.setCharset("GB2312");
		site.setDebug(false);
		site.setRealTime(true);
		site.setDeployTimeSelector("div[class=title]>span");
		site.setPageSelector("div[id=pager]>ul>li>a");
		site.setContentSelector("div[id=tbcontent]>p");
		site.setName(Site.SD_INFO);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(982L);
		site.setChannelName("济南新闻");
		site.setUrl("http://news.sdinfo.net/sdxw/jnxw/default.shtml");
		site.setPageSuffix("jnxw/\\d+.shtml");
		site.setDatePattern("yyyy-MM-dd");
		site.setCharset("GB2312");
		site.setDebug(false);
		site.setRealTime(true);
		site.setDeployTimeSelector("div[class=title]>span");
		site.setPageSelector("div[id=pager]>ul>li>a");
		site.setContentSelector("div[id=tbcontent]>p");
		site.setName(Site.SD_INFO);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(983L);
		site.setChannelName("青岛新闻");
		site.setUrl("http://news.sdinfo.net/sdxw/qdxw/default.shtml");
		site.setPageSuffix("qdxw/\\d+.shtml");
		site.setDatePattern("yyyy-MM-dd");
		site.setCharset("GB2312");
		site.setDebug(false);
		site.setRealTime(true);
		site.setDeployTimeSelector("div[class=title]>span");
		site.setPageSelector("div[id=pager]>ul>li>a");
		site.setContentSelector("div[id=tbcontent]>p");
		site.setName(Site.SD_INFO);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(1082L);
		site.setChannelName("山东各地");
		site.setUrl("http://news.sdinfo.net/sdxw/gdxw/default.shtml");
		site.setPageSuffix("gdxw/\\d+.shtml");
		site.setDatePattern("yyyy-MM-dd");
		site.setCharset("GB2312");
		site.setDebug(false);
		site.setRealTime(true);
		site.setDeployTimeSelector("div[class=title]>span");
		site.setPageSelector("div[id=pager]>ul>li>a");
		site.setContentSelector("div[id=tbcontent]>p");
		site.setName(Site.SD_INFO);
		sites.add(site);
		*/
		String configPath = "conf/com.bcinfo.crawl.channel.news.sdinfo.xml";
		sites.addAll(new SiteUtil().getSites(configPath));
		return sites;
	}
	
	public void start(BundleContext context) throws Exception {
		log.info("新闻频道-山东-抓取程序启动");
		
		int corePoolSize = getSites().size();
		scheduled = Executors.newScheduledThreadPool(corePoolSize);
		
		CrawlerLogService crawlerLogService = (CrawlerLogService)context.getService(context.getServiceReference(CrawlerLogService.class.getName()));
		WebCrawlerDao webCrawlerDao = (WebCrawlerDao)context.getService(context.getServiceReference(WebCrawlerDao.class.getName()));
		int index = 1;
		for(Site site : getSites()) {
			SiteParser siteParser = new SiteParser();
			siteParser.setSite(site);
			siteParser.setWebCrawlerDao(webCrawlerDao);
			siteParser.setCrawlerLogService(crawlerLogService);
			scheduled.scheduleAtFixedRate(siteParser, index, site.getFrequency(), TimeUnit.SECONDS);
			index++;
		}
		
	}

	public void stop(BundleContext context) throws Exception {
		if(scheduled != null) scheduled.shutdown();
		log.info("新闻频道-山东-抓取程序关闭");
	}

}
