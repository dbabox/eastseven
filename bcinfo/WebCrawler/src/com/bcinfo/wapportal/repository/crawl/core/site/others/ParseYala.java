/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-11-11 上午09:18:58<br>
 *         www.51yala.com
 */
public class ParseYala extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseYala.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("取51yala[" + link + "]分页失败");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {
			content = this.getPageContent(link, "span", "class", "STYLE3");
			content = this.commonParseContent(content);
			
			content = content.replaceAll("<xoYu_CMS_CODE:NEWS:NEWS_INFO>", replacement);
			content = content.replaceAll("</xoYu_CMS_CODE:NEWS:NEWS_INFO>", replacement);
		} catch (Exception e) {
			System.out.println("解析51YALA页面[" + link + "]内容失败");
			if(log.isDebugEnabled()) e.printStackTrace();
		}

		return content;
	}

	@Override
	public String parse(String link) {
		return this.simpleParse(link);
	}

	@Override
	public Boolean parse(String folderId, String title, String link) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		String link = "http://www.51yala.com/Html/20071522056-1.html";
		String cnt = new ParseYala().parse(link);
		System.out.println(cnt);
	}
}
