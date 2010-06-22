/**
 * 
 */
package com.bcinfo.crawl.site.lottery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Site;
import com.bcinfo.crawl.site.lottery.service.LotteryParser;

/**
 * @author dongq
 * 
 *         create time : 2010-5-25 ÏÂÎç05:32:02
 */
public final class LotteryJob implements Job {

	private WebCrawlerDao dao;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		dao = (WebCrawlerDao)context.getJobDetail().getJobDataMap().get("WebCrawlerDao");
		start();
	}

	private void start() {
		List<Site> sites = getCrawlSites();
		for(Site site : sites) {
			new Thread(new LotteryParser(site, dao)).start();
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Site> getCrawlSites() {
		List<Site> sites = new ArrayList<Site>();

		try {
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(getClass().getResourceAsStream("sites.xml"));
			Element root = doc.getRootElement();
			List siteList = root.getChildren("site");
			for(Iterator iter = siteList.iterator(); iter.hasNext(); ) {
				Site site = new Site();
				Element siteElement = (Element)iter.next();
				site.setChannelId(Long.parseLong(siteElement.getChild("channelId").getText()));
				site.setChannelName(siteElement.getChild("channelName").getText());
				site.setCharset(siteElement.getChild("charset").getText());
				site.setUrl(siteElement.getChild("url").getText());
				sites.add(site);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sites;
	}
}
