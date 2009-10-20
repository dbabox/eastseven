/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 обнГ01:09:28
 */
public interface Parse {
	
	public String parse(String link);
	
	public Boolean parse(String folderId, String title, String link);
}
