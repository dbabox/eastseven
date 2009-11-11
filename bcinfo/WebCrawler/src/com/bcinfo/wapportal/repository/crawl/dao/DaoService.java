/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.util.List;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;

/**
 * @author dongq
 * 
 *         create time : 2009-10-15 下午01:42:08<br>
 *         数据库相关操作服务
 *
 */
public interface DaoService {

	public List<CrawlList> getCrawlLists();

	public List<CrawlList> getCrawlLists(Long folderId);

	public Boolean isExistCrawlResource(Long channelId, String title);
	
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
}
