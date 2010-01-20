/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.service;

import java.util.List;
import java.util.Map;

/**
 * @author dongq
 * 
 *         create time : 2009-10-20 ����04:42:11
 */
public interface CrawlResourceService {

	/**
	 * �Զ�����
	 * @return
	 */
	public Boolean sendResourceAuto();

	/**
	 * �ֶ�����
	 * @param userId
	 * @param channelId
	 * @param resourceIds
	 * @param map
	 * @return
	 */
	public Boolean sendResource(Long userId, String channelId, List<Long> resourceIds, Map<String, Object> map);
	
	/**
	 * �ֶ�����
	 * @param userId
	 * @param channelId
	 * @param resourceIds
	 * @param map
	 * @return
	 */
	public Boolean sendResource(Long userId, String channelId, String[] resourceIds, Map<String, Object> map);
	
	
}
