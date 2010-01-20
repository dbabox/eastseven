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
	
	public static final String TAG_NAME = "tagName";
	public static final String ATTRIBUTE_NAME = "attrName";
	public static final String ATTRIBUTE_VALUE = "attrValue";
	
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
