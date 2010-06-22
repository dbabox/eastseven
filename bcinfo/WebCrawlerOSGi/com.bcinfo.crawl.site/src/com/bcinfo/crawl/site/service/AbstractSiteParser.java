/**
 * 
 */
package com.bcinfo.crawl.site.service;

/**
 * @author dongq
 * 
 *         create time : 2010-6-11 обнГ02:59:46
 */
public abstract class AbstractSiteParser implements ISiteParser {

	@Override
	public void run() {
		crawlSiteLinks();
	}
}
