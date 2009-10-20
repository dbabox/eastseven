/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

/**
 * @author dongq
 * 
 *         create time : 2009-9-16 上午10:35:29<br>
 *         针对消息来源做相应的处理
 */
public final class PublisherUtil {

	/**
	 * 新浪体育
	 */
	public final static String SINA_SPORTS = "（来源：四川新闻网-新浪体育）";
	
	/**
	 * 搜狐娱乐
	 */
	public final static String SOHU_ENT = "（来源：四川新闻网-搜狐娱乐）";
	
	/**
	 * TOM娱乐
	 */
	public final static String TOM_ENT = "（来源：四川新闻网-TOM娱乐）";
	
	/**
	 * 腾讯娱乐
	 */
	public final static String QQ_ENT = "（来源：四川新闻网-腾讯娱乐）";
	
	/**
	 * 根据网址，取得消息来源
	 * @param url
	 * @return（来源：四川新闻网-XXXX）
	 */
	public static String addMsgOrigin(String url){
		String msgOrigin = "  ";
		
		try{
			url = url.toLowerCase();
			if(url.indexOf("sports")!=-1){
				if(url.indexOf("sina")!=-1) msgOrigin += SINA_SPORTS;
			}
			if(url.indexOf("ent")!=-1){
				if(url.indexOf("qq")!=-1) msgOrigin += QQ_ENT;
				if(url.indexOf("tom")!=-1) msgOrigin += TOM_ENT;
				if(url.indexOf("sohu")!=-1) msgOrigin += SOHU_ENT;
			}
			
		}catch(Exception e){
			return "（来源：四川新闻网）";
		}
		
		return msgOrigin;
	}
}
