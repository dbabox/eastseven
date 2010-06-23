package com.bcinfo.crawl.channel.mobile;

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
		site.setChannelId(282L);
		site.setChannelName("手机新闻");
		site.setUrl("http://digi.tech.qq.com/l/mobile/02new/list20100329144655.htm");
		site.setPageSuffix("digi.*?/\\d{4}\\d{2}\\d{2}/\\d+.htm");
		site.setDatePattern("yyyyMMdd");
		site.setCharset("GB2312");
		site.setDebug(false);
		//site.setRealTime(false);
		site.setPageSelector("div[id=ArtPLink]>ul>li>a");
		site.setContentSelector("div[id=Cnt-Main-Article-QQ]");
		site.setName(Site.QQ);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(283L);
		site.setChannelName("手机行情");
		site.setUrl("http://tech.sina.com.cn/mobile/market/qbgd/roll/index.html");
		site.setPageSuffix("tech.*?mobile.*?\\d{4}-\\d{2}-\\d{2}/\\d+.shtml");
		site.setDatePattern("yyyy-MM-dd");
		site.setCharset("GB2312");
		site.setDebug(false);
		//site.setRealTime(false);
		site.setPageSelector("span[id=_function_code_page]>p>a");
		site.setContentSelector("div[id=artibody]>p");
		site.setName(Site.SINA);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(283L);
		site.setChannelName("手机导购");
		site.setUrl("http://digi.tech.qq.com/l/mobile/02show/list20100329164413.htm");
		site.setPageSuffix("digi.*?/\\d{4}\\d{2}\\d{2}/\\d+.htm");
		site.setDatePattern("yyyyMMdd");
		site.setCharset("GB2312");
		site.setDebug(false);
		//site.setRealTime(false);
		site.setPageSelector("div[id=ArtPLink]>ul>li>a");
		site.setContentSelector("div[id=Cnt-Main-Article-QQ]");
		site.setName(Site.QQ);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(285L);
		site.setChannelName("手机测评");
		site.setUrl("http://tech.sina.com.cn/mobile/preview/roll/index.html");
		site.setPageSuffix("tech.*?mobile.*?\\d{4}-\\d{2}-\\d{2}/\\d+.shtml");
		site.setDatePattern("yyyy-MM-dd");
		site.setCharset("GB2312");
		site.setDebug(false);
		//site.setRealTime(false);
		site.setPageSelector("span[id=_function_code_page]>p>a");
		site.setContentSelector("div[id=artibody]>p");
		site.setName(Site.SINA);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(286L);
		site.setChannelName("手机图赏");
		site.setUrl("http://tech.sina.com.cn/mobile/photos/NOKIA.html");
		site.setPageSuffix("tech.*?mobile.*?\\d{4}-\\d{2}-\\d{2}/\\d+.shtml");
		site.setDatePattern("yyyy-MM-dd");
		site.setCharset("GB2312");
		site.setDebug(false);
		//site.setRealTime(false);
		site.setPageSelector("span[id=_function_code_page]>p>a");
		site.setContentSelector("div[id=artibody]>p");
		site.setName(Site.SINA);
		sites.add(site);
		*/
		String configPath = "conf/com.bcinfo.crawl.channel.mobile.xml";
		sites.addAll(new SiteUtil().getSites(configPath));
		return sites;
	}
	
	public void start(BundleContext context) throws Exception {
		log.info("手机频道抓取程序启动");
		
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
		log.info("手机频道抓取程序关闭");
	}

}
