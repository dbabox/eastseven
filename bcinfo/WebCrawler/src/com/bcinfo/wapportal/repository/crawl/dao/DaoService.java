/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.util.List;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.internal.AppLog;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;

/**
 * @author dongq
 * 
 *         create time : 2009-10-15 下午01:42:08<br>
 *         数据库相关操作服务
 *
 */
public interface DaoService {

	public List<CrawlList> getCrawlLists(String status);

	public List<CrawlList> getCrawlLists(Long folderId);

	public Boolean isExistCrawlResource(Long channelId, String title);
	
	/**
	 * 取得频道名称
	 * @param channelId
	 * @return
	 */
	public String getChannelName(Long channelId);
	
	/**
	 * 保存抓取资源
	 * @param folders
	 * @return
	 */
	public Boolean saveCrawlResource(List<FolderBO> folders);

	/**
	 * 清除七天之前的抓取数据
	 * @return
	 */
	public Boolean clearCrawlResource();
	
	/**
	 * 删除频道下的所有数据,改用存储过程实现,次方法废弃
	 * @param channelId
	 * @return
	 */
	public Boolean deleteCrawlResource(Long channelId);
	
	/**
	 * 抓取日志记录
	 * @param appLog
	 * @return
	 */
	public Boolean saveLog(AppLog appLog);
	
	@Deprecated
	public Boolean isAnyTriggerFired();
	
	/**
	 * 初始化Quartz数据库
	 * @param sqlFile
	 * @return
	 */
	public Boolean initQuartzDatabase(String sqlFile);
	
}
