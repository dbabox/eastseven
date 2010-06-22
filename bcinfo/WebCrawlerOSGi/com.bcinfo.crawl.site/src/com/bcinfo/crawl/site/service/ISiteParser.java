/**
 * 
 */
package com.bcinfo.crawl.site.service;

import java.util.List;

/**
 * @author dongq
 * 
 *         create time : 2010-6-11 обнГ02:54:34
 */
public interface ISiteParser extends Runnable {

	public void crawlSiteLinks();
	
	public void crawlPageContent();
	
	public List<String> getPageLinks(String pageLink, String pageEncoding);
}
