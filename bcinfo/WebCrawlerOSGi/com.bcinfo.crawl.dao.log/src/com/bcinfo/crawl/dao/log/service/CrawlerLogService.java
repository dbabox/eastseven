/**
 * 
 */
package com.bcinfo.crawl.dao.log.service;

import java.util.List;

import com.bcinfo.crawl.domain.model.CrawlerLog;

/**
 * @author dongq
 * 
 *         create time : 2010-6-1 ����10:36:55
 */
public interface CrawlerLogService {

	/**
	 * ����ץȡ���ĵ�ַ
	 * @param crawlerLog
	 * @return
	 */
	public boolean save(CrawlerLog crawlerLog);
	
	/**
	 * ����ץȡ���ĵ�ַ
	 * @param crawlerLogs
	 * @return
	 */
	public boolean save(CrawlerLog[] crawlerLogs);
	
	/**
	 * ȡ��ץȡ���ĵ�ַ��
	 * @param id
	 * @return
	 */
	public List<CrawlerLog> get(Long id);
	
}
