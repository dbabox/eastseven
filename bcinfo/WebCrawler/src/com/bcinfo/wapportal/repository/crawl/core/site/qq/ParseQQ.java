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
			if(content == null || "".equals(content)){
				content = this.getPageContent(link, "id", "Cnt-Main-Article-QQ");
			}
			content = this.commonParseContent(content);
			
			//视频链接Object标签
			content = RegexUtil.eliminateString("<[oO][bB][jJ][eE][cC][tT]\\s+[^>]+>", "</[oO][bB][jJ][eE][cC][tT]>", content);
			content = content.replaceAll("搜索华尔兹--今日热辣资讯Top100", replacement);
			content = content.replaceAll("<style>.*?</style>", replacement);
			//content = 
		}catch(Exception e){
			System.out.println("解析QQ页面["+link+"]内容失败");
		}
		
		return content;
	}

	//TODO TEST
	public static void main(String[] args) {
		String link = "http://sports.qq.com/a/20091130/000761.htm";
		ParseQQ p = new ParseQQ();
		System.out.println(p.parse(link));
	}
}
