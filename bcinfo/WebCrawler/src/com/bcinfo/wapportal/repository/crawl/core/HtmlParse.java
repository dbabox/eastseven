/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.util.List;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 上午10:24:13
 */
public interface HtmlParse {

	/**
	 * 取得可用的地址集合<br>
	 * 1.0
	 * @param url
	 * @return
	 */
	public List<FolderBO> getUsableCrawlLink(String folderId, String url);
	
}
