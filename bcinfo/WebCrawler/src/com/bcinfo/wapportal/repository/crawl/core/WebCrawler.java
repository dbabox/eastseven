/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.util.List;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����10:02:08
 */
public interface WebCrawler {

	/**
	 * ��ȡ���<br>
	 * 1.0
	 * @param folderId Ŀ����ĿID
	 * @param url ��ȡ��ַ
	 * @return ��Ŀ����
	 */
	public List<FolderBO> crawl(String folderId, String url);
	
	
}
