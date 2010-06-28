/**
 * 
 */
package com.bcinfo.crawl.site.service;

import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Resource;
import com.bcinfo.crawl.domain.model.Site;

/**
 * @author dongq
 * 
 *         create time : 2010-6-11 обнГ02:54:34
 */
public interface ISiteParser extends Runnable {

	public void crawlSiteStart();
	
	public void crawlPageContent(Resource resource);
	
	public void setCrawlerLogService(CrawlerLogService crawlerLogService);

	public void setWebCrawlerDao(WebCrawlerDao webCrawlerDao);

	public void setSite(Site site);
}
