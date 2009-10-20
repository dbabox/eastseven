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
 *         create time : 2009-10-15 обнГ01:42:08
 */
public interface DaoService {

	public List<CrawlList> getCrawlLists();
	
	public List<CrawlList> getCrawlLists(Long folderId);
	
	public Boolean saveCrawlResource(List<FolderBO> folders);
}
