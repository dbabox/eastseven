/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.service;

/**
 * @author dongq
 * 
 *         create time : 2009-10-20 下午04:42:11
 */
public interface CrawlResourceService {

	/**
	 * 自动发送
	 * @return
	 */
	public Boolean sendResourceAuto();
	
	/**
	 * 手动发送
	 * @param userId
	 * @param channelId
	 * @param resourceIds
	 * @return
	 */
	public Boolean sendResource(Long userId, String channelId, String[] resourceIds);
	
}
