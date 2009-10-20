/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.util.List;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 上午10:02:08
 */
public interface WebCrawler {

	/**
	 * 爬取入口<br>
	 * 1.0
	 * @param folderId 目标栏目ID
	 * @param url 爬取地址
	 * @return 栏目集合
	 */
	public List<FolderBO> crawl(String folderId, String url);
	
	
}
