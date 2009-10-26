/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.xinhuanet;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 下午01:56:56
 */
public class ParseXinHuaNet extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseXinHuaNet.class);
	
	@Override
	public String parse(String link) {
		return this.simpleParse(link);
	}
	
	@Override
	public Boolean parse(String folderId, String title, String link) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("取新华网["+link+"]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "class", "xilanwz-x");
			if("".equals(content))
				content = this.getPageContent(link, "id", "Content");
			content = this.commonParseContent(content);
			
			//新华网
			content = content.replaceAll("\\[进入.*论坛\\]", replacement);
			content = content.replaceAll("发表您的观点。请您文明上网、理性发言并遵守相关规定，在注册后发表评论。  留言须知", replacement);
			content = content.replaceAll("=", replacement);
			
			if(log.isDebugEnabled()){
				System.out.println("------------------------ParseXinHuaNet-------------------------------");
				System.out.println(content);
			}
		}catch(Exception e){
			System.out.println("解析新华网页面["+link+"]内容失败");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		
		return content;
	}

}
