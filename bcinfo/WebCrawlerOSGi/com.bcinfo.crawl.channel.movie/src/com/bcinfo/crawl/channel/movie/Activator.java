package com.bcinfo.crawl.channel.movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.bcinfo.crawl.channel.movie.service.SiteParser;
import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Site;

public class Activator implements BundleActivator {

	private static final Log log = LogFactory.getLog(Activator.class);
	
	private ScheduledExecutorService scheduled = null;
	
	public List<Site> getSites() throws Exception {
		Site site = null;
		List<Site> sites = new ArrayList<Site>();
		
		site = new Site();
		site.setName(Site.SINA);
		site.setChannelId(1321L);
		site.setChannelName("影评世界");
		site.setCharset("GB2312");
		site.setUrl("http://ent.sina.com.cn/review/media/film/more.html");
		site.setRealTime(true);
		site.setDebug(false);
		site.setPageSuffix("\\d{4}-\\d{2}-\\d{2}/\\d+.shtml");
		site.setDatePattern("yyyy-MM-dd");
		site.setContentSelector("div[id=artibody]");
		sites.add(site);
		
		site = new Site();
		site.setName(Site.SOHU);
		site.setChannelId(1321L);
		site.setChannelName("影评世界");
		site.setCharset("GB2312");
		site.setUrl("http://yule.sohu.com/dypst/");
		site.setRealTime(true);
		site.setDebug(false);
		site.setPageSuffix("\\d{4}\\d{2}\\d{2}/n\\d+.shtml");
		site.setPageSelector("select[name=gotopage]>option");
		site.setDatePattern("yyyyMMdd");
		site.setContentSelector("div[id=contentText]");
		sites.add(site);
		
		site = new Site();
		site.setUrl("http://yule.sohu.com/guoneidianying.shtml");
		site.setName(Site.SOHU);
		site.setChannelId(1321L);
		site.setChannelName("影评世界");
		site.setCharset("GB2312");
		site.setRealTime(true);
		site.setDebug(false);
		site.setPageSuffix("\\d{4}\\d{2}\\d{2}/n\\d+.shtml");
		site.setPageSelector("select[name=gotopage]>option");
		site.setDatePattern("yyyyMMdd");
		site.setContentSelector("div[id=contentText]");
		sites.add(site);
		
		site = new Site();
		site.setUrl("http://yule.sohu.com/guoneidianying.shtml");
		site.setChannelId(1322L);
		site.setChannelName("电影资讯");
		site.setName(Site.SOHU);
		site.setCharset("GB2312");
		site.setRealTime(true);
		site.setDebug(false);
		site.setPageSuffix("\\d{4}\\d{2}\\d{2}/n\\d+.shtml");
		site.setPageSelector("select[name=gotopage]>option");
		site.setDatePattern("yyyyMMdd");
		site.setContentSelector("div[id=contentText]");
		sites.add(site);
		
		site = new Site();
		site.setUrl("http://yule.sohu.com/guowaidongtai.shtml");
		site.setChannelId(1322L);
		site.setChannelName("电影资讯");
		site.setName(Site.SOHU);
		site.setCharset("GB2312");
		site.setRealTime(true);
		site.setDebug(false);
		site.setPageSuffix("\\d{4}\\d{2}\\d{2}/n\\d+.shtml");
		site.setPageSelector("select[name=gotopage]>option");
		site.setDatePattern("yyyyMMdd");
		site.setContentSelector("div[id=contentText]");
		sites.add(site);
		
		return sites;
	}
	
	public void start(BundleContext context) throws Exception {
		
		log.info("电影频道抓取启动");
		List<Site> sites = getSites();
		int corePoolSize = sites.size();
		scheduled = Executors.newScheduledThreadPool(corePoolSize);
		
		CrawlerLogService crawlerLogService = (CrawlerLogService)context.getService(context.getServiceReference(CrawlerLogService.class.getName()));
		WebCrawlerDao webCrawlerDao = (WebCrawlerDao)context.getService(context.getServiceReference(WebCrawlerDao.class.getName()));
		for(Site site : sites) {
			SiteParser siteParser = new SiteParser();
			siteParser.setSite(site);
			siteParser.setWebCrawlerDao(webCrawlerDao);
			siteParser.setCrawlerLogService(crawlerLogService);
			scheduled.scheduleAtFixedRate(siteParser, 0, 1, TimeUnit.HOURS);
			Thread.sleep(10 * 1000);
		}
		
	}

	public void stop(BundleContext context) throws Exception {
		if(scheduled != null) {
			scheduled.shutdown();
		}
		log.info("电影频道抓取关闭");
	}

}
