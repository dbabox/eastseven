/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����01:09:28
 */
public interface Parse {
	
	/**
	 * ������ҳ����
	 * @param link
	 * @return
	 */
	public String parse(String link);
	
	/**
	 * ������ҳ����,Ԥ��
	 * @param folderId
	 * @param title
	 * @param link
	 * @return
	 */
	public Boolean parse(String folderId, String title, String link);
}
