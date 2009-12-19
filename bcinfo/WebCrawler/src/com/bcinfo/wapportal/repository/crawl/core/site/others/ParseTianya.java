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
 *         create time : 2009-12-1 上午11:01:17<br>
 *         天涯论坛<br>
 */
public class ParseTianya extends AbstractHtmlParseTemplete implements Parse{

	private static final Logger log = Logger.getLogger(ParseTianya.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			log.info("取天涯论坛["+link+"]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "class", "content");
			if(content!=null&&!"".equals(content)){
				content = content.substring(0, content.indexOf("<TABLE"));
				content = this.commonParseContent(content);
			}
			
		}catch(Exception e){
			log.info("解析天涯页面["+link+"]内容失败");
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
	
	//TODO TEST
	public static void main(String[] args) {
		String link = "http://www.tianya.cn/publicforum/content/fans/1/173695.shtml";
		ParseTianya p = new ParseTianya();
		//System.out.println(p.parse(link));
	}
}
