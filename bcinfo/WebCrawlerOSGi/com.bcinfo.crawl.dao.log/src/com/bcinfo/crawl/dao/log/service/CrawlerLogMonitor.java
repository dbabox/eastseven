/**
 * 
 */
package com.bcinfo.crawl.dao.log.service;

import java.util.List;
import java.util.Map;

/**
 * @author dongq
 * 
 *         create time : 2010-7-13 上午10:33:51<br>
 *         删除抓取记录
 *         打印抓取记录
 */
public interface CrawlerLogMonitor {

	/**
	 * 清除指定频道的日志记录
	 * @param channelId
	 * @return
	 */
	public boolean clearCrawlerLogs(long channelId);
	
	/**
	 * 显示所有频道的日志记录
	 * @return
	 */
	public void displayAllCrawlerLogs();
	
	public List<Map<String, String>> getAllCrawlerLogs();
	
	/**
	 * 显示指定频道的日志记录
	 * @param channelId
	 * @return
	 */
	public void displayCrawlerLogs(long channelId);
	
	public List<Map<String, String>> getCrawlerLogs(long channelId);
	
	public List<String> getChannelIds();
}
