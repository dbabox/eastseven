/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.wangyi;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-12-1 上午10:02:49<br>
 *         网易<br>
 */
public class Parse163 extends AbstractHtmlParseTemplete implements Parse {

	private static final Logger log = Logger.getLogger(Parse163.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			log.info("取163["+link+"]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "endText");
			content = this.commonParseContent(content);
			
		}catch(Exception e){
			log.info("解析163页面["+link+"]内容失败");
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

	//TODO TEST
	public static void main(String[] args) {
		String link = "http://sports.163.com/09/1202/04/5PGHPCLM00051CCL.html";
		Parse163 p = new Parse163();
		System.out.println(p.parse(link));
	}
}
