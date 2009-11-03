/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ÏÂÎç01:09:28
 */
public interface Parse {
	
	/**
	 * ½âÎöÍøÒ³ÄÚÈİ
	 * @param link
	 * @return
	 */
	public String parse(String link);
	
	/**
	 * ½âÎöÍøÒ³ÄÚÈİ,Ô¤Áô
	 * @param folderId
	 * @param title
	 * @param link
	 * @return
	 */
	public Boolean parse(String folderId, String title, String link);
}
