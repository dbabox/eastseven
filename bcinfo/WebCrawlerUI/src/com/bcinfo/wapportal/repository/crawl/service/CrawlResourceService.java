/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.service;

import java.util.List;
import java.util.Map;

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
	 * @param map
	 * @return
	 */
	public Boolean sendResource(Long userId, String channelId, List<Long> resourceIds, Map<String, Object> map);
	
	/**
	 * 手动发送
	 * @param userId
	 * @param channelId
	 * @param resourceIds
	 * @param map
	 * @return
	 */
	public Boolean sendResource(Long userId, String channelId, String[] resourceIds, Map<String, Object> map);
	
	
}
