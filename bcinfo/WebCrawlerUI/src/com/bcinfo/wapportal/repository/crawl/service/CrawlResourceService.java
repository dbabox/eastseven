/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.service;

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
	 * @return
	 */
	public Boolean sendResource(Long userId, String channelId, String[] resourceIds);
	
}
