/**
 * 
 */
package com.bcinfo.crawl.dao.log.service;

import java.util.List;

import com.bcinfo.crawl.domain.model.CrawlerLog;

/**
 * @author dongq
 * 
 *         create time : 2010-6-1 上午10:36:55
 */
public interface CrawlerLogService {

	/**
	 * 保存抓取过的地址
	 * @param crawlerLog
	 * @return
	 */
	public boolean save(CrawlerLog crawlerLog);
	
	/**
	 * 保存抓取过的地址
	 * @param crawlerLogs
	 * @return
	 */
	public boolean save(CrawlerLog[] crawlerLogs);
	
	/**
	 * 取得抓取过的地址集
	 * @param id
	 * @return
	 */
	public List<CrawlerLog> get(Long id);
	
}
