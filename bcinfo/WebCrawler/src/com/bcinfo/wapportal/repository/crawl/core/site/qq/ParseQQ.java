/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.qq;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 * create time : 2009-10-14 下午01:01:53
 */
public class ParseQQ extends AbstractHtmlParseTemplete implements Parse {

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
			System.out.println("取QQ["+link+"]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			//ArticleCnt
			content = this.getPageContent(link, "id", "ArticleCnt");
			content = this.commonParseContent(content);
			
			//视频链接Object标签
			content = RegexUtil.eliminateString("<[oO][bB][jJ][eE][cC][tT]\\s+[^>]+>", "</[oO][bB][jJ][eE][cC][tT]>", content);
		}catch(Exception e){
			System.out.println("解析QQ页面["+link+"]内容失败");
		}
		
		return content;
	}

}
