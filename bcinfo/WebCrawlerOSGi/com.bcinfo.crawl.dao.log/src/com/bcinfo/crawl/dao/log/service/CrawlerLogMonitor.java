/**
 * 
 */
package com.bcinfo.crawl.dao.log.service;

import java.util.List;
import java.util.Map;

/**
 * @author dongq
 * 
 *         create time : 2010-7-13 ����10:33:51<br>
 *         ɾ��ץȡ��¼
 *         ��ӡץȡ��¼
 */
public interface CrawlerLogMonitor {

	/**
	 * ���ָ��Ƶ������־��¼
	 * @param channelId
	 * @return
	 */
	public boolean clearCrawlerLogs(long channelId);
	
	/**
	 * ��ʾ����Ƶ������־��¼
	 * @return
	 */
	public void displayAllCrawlerLogs();
	
	public List<Map<String, String>> getAllCrawlerLogs();
	
	/**
	 * ��ʾָ��Ƶ������־��¼
	 * @param channelId
	 * @return
	 */
	public void displayCrawlerLogs(long channelId);
	
	public List<Map<String, String>> getCrawlerLogs(long channelId);
	
	public List<String> getChannelIds();
}
