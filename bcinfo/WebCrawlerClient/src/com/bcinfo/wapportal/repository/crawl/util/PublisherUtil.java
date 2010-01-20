/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author dongq
 * 
 *         create time : 2009-9-16 上午10:35:29<br>
 *         针对消息来源做相应的处理
 */
public final class PublisherUtil {

	private static Logger log = Logger.getLogger(PublisherUtil.class);
	
	/**
	 * 新浪体育
	 */
	public final static String SINA_SPORTS = "（来源：四川新闻网-新浪体育）";
	public final static String SOHU_SPORTS = "（来源：四川新闻网-搜狐体育）";
	public final static String QQ_SPORTS = "（来源：四川新闻网-腾讯体育）";
	public final static String WANGYI_SPORTS = "（来源：四川新闻网-网易体育）";
	public final static String IFENG = "（来源：四川新闻网-凤凰网）";
	
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
	 * 新华网
	 */
	public final static String XINHUA_NEWS = "（来源：四川新闻网-新华网）";
	
	/**
	 * 根据网址，取得消息来源<br>
	 * 针对不同省市，区别对待<br>
	 * @param url
	 * @return（来源：四川新闻网-XXXX）
	 */
	public static String addMsgOrigin(String url){
		String msgOrigin = "  ";
		
		Properties property = new ConfigPropertyUtil().getConfigProperty();
		if(property!=null){
			String area = property.getProperty("area");
			if(area!=null&&!"".equals(area)){
				if("028".equals(area)){
					try{
						url = url.toLowerCase();
						if(url.indexOf("sports")!=-1){
							//按运营的要求，全部统一成新浪体育
							msgOrigin += SINA_SPORTS;
//							if(url.indexOf("sina")!=-1) msgOrigin += SINA_SPORTS;
//							if(url.indexOf("163")!=-1) msgOrigin += WANGYI_SPORTS;
//							if(url.indexOf("qq")!=-1) msgOrigin += QQ_SPORTS;
//							if(url.indexOf("sohu")!=-1) msgOrigin += SOHU_SPORTS;
//							if(url.indexOf("ifeng")!=-1) msgOrigin += IFENG;
						}
						if(url.indexOf("ent")!=-1){
							if(url.indexOf("qq")!=-1) msgOrigin += QQ_ENT;
							if(url.indexOf("tom")!=-1) msgOrigin += TOM_ENT;
							if(url.indexOf("sohu")!=-1) msgOrigin += SOHU_ENT;
						}
						if(url.indexOf("xinhuanet")!=-1 || url.indexOf("www.news.cn")!=-1){
							msgOrigin += XINHUA_NEWS;
						}
					}catch(Exception e){
						return "（来源：四川新闻网）";
					}
				}else if("0791".equals(area)){
					return "（来源：新华网）";
				}
			}else{
				//返回默认值
			}
		}
		
		return msgOrigin;
	}
	
}
