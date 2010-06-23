package com.bcinfo.crawl.site.xinhuanet;

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
		boolean debug = false;
		site = new Site();
		site.setChannelId(2L);
		site.setChannelName("国际新闻");
		site.setUrl("http://www.xinhuanet.com/world/tt.htm");
		site.setPageSuffix("world/\\d{4}-\\d{2}/\\d{2}/c_\\d+.htm");
		site.setDatePattern("yyyy-MM/dd");
		site.setCharset("UTF-8");
		site.setDebug(debug);
		site.setRealTime(true);
		site.setPageSelector("div[id=div_currpage]>a");
		site.setContentSelector("div[id=Content]>p");
		site.setName(Site.XIN_HUA_NET);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(4L);
		site.setChannelName("社会新闻");
		site.setUrl("http://www.xinhuanet.com/society/");
		site.setPageSuffix("society/\\d{4}-\\d{2}/\\d{2}/c_\\d+.htm");
		site.setDatePattern("yyyy-MM/dd");
		site.setCharset("UTF-8");
		site.setDebug(debug);
		site.setRealTime(true);
		site.setPageSelector("div[id=div_currpage]>a");
		site.setContentSelector("div[id=Content]>p");
		site.setName(Site.XIN_HUA_NET);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(5L);
		site.setChannelName("四川新闻");
		site.setUrl("http://www.sc.xinhuanet.com/");
		site.setPageSuffix(".sc.*?\\d{4}-\\d{2}/\\d{2}/content_\\d+.htm");
		site.setDatePattern("yyyy-MM/dd");
		site.setCharset("GB2312");
		site.setDebug(debug);
		site.setRealTime(true);
		site.setContentSelector("div[id=xhw]>p");
		site.setName(Site.XIN_HUA_NET);
		sites.add(site);
		
		site = new Site();
		site.setUrl("http://www.jx.xinhuanet.com/news/society/");
		site.setChannelId(941L);
		site.setChannelName("江西新闻");
		site.setPageSuffix(".jx.*?\\d{4}-\\d{2}/\\d{2}/content_\\d+.htm");
		site.setDatePattern("yyyy-MM/dd");
		site.setCharset("GB2312");
		site.setDebug(false);
		site.setRealTime(true);
		site.setPageSelector("p[class=pagelink]>a");
		site.setContentSelector("div[class=xilanwz-x]>p");
		site.setName(Site.XIN_HUA_NET);
		sites.add(site);
		
		site = new Site();
		site.setUrl("http://www.jx.xinhuanet.com/news/focus/");
		site.setChannelId(941L);
		site.setChannelName("江西新闻");
		site.setPageSuffix(".jx.*?\\d{4}-\\d{2}/\\d{2}/content_\\d+.htm");
		site.setDatePattern("yyyy-MM/dd");
		site.setCharset("GB2312");
		site.setDebug(false);
		site.setRealTime(true);
		site.setPageSelector("p[class=pagelink]>a");
		site.setContentSelector("div[class=xilanwz-x]>p");
		site.setName(Site.XIN_HUA_NET);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(122L);
		site.setChannelName("科技新闻");
		site.setUrl("http://www.xinhuanet.com/tech/index.htm");
		site.setPageSuffix("tech/\\d{4}-\\d{2}/\\d{2}/c_\\d+.htm");
		site.setDatePattern("yyyy-MM/dd");
		site.setCharset("UTF-8");
		site.setDebug(debug);
		site.setRealTime(true);
		site.setPageSelector("div[id=div_currpage]>a");
		site.setContentSelector("div[id=Content]>p");
		site.setName(Site.XIN_HUA_NET);
		sites.add(site);
		
		site = new Site();
		site.setChannelId(585L);
		site.setChannelName("金融理财");
		site.setUrl("http://www.xinhuanet.com/finance/");
		site.setPageSuffix("finance/\\d{4}-\\d{2}/\\d{2}/c_\\d+.htm");
		site.setDatePattern("yyyy-MM/dd");
		site.setCharset("UTF-8");
		site.setDebug(debug);
		site.setRealTime(true);
		site.setPageSelector("div[id=div_currpage]>a");
		site.setContentSelector("div[id=Content]>p");
		site.setName(Site.XIN_HUA_NET);
		sites.add(site);
		*/
		String configPath = "conf/com.bcinfo.crawl.site.xinhuanet.xml";
		sites.addAll(new SiteUtil().getSites(configPath));
		return sites;
	}
	
	public void start(BundleContext context) throws Exception {
		log.info("站点-新华网-抓取程序启动");
		
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
			int start = (index==1) ? 0 : index;
			scheduled.scheduleAtFixedRate(siteParser, start, site.getFrequency(), TimeUnit.SECONDS);
			index++;
		}
		
	}

	public void stop(BundleContext context) throws Exception {
		if(scheduled != null) scheduled.shutdown();
		log.info("站点-新华网-抓取程序关闭");
	}

}
