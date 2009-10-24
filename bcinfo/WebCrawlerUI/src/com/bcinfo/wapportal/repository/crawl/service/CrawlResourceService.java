/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.service;

/**
 * @author dongq
 * 
 *         create time : 2009-10-20 обнГ04:42:11
 */
public interface CrawlResourceService {

	public Boolean sendResource(Long userId, String channelId, String[] resourceIds);
}
