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
 *         create time : 2009-12-1 ÉÏÎç10:21:00<br>
 *         ·ï»ËÍø<br>
 */
public class ParseIFeng extends AbstractHtmlParseTemplete implements Parse {

	private static final Logger log = Logger.getLogger(ParseIFeng.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			log.info("È¡·ï»ËÍø["+link+"]·ÖÒ³Ê§°Ü");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "artical_real");
			content = this.commonParseContent(content);
			
		}catch(Exception e){
			log.info("½âÎö·ï»ËÍøÒ³Ãæ["+link+"]ÄÚÈÝÊ§°Ü");
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
		String link = "http://news.ifeng.com/sports/pinglun/200912/1201_4687_1457658.shtml";
		ParseIFeng p = new ParseIFeng();
		System.out.println(p.parse(link));
	}
}
