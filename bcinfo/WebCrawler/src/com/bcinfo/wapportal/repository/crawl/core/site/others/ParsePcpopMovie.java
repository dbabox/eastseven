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
 *         create time : 2009-11-3 …œŒÁ11:21:08
 */
public class ParsePcpopMovie extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParsePcpopMovie.class);

	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("»°MOVIE.PCPOP[" + link + "]∑÷“≥ ß∞‹");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {
			String tagName = "div";
			String attrName = "class";
			String attrValue = "movb32";
			
			//Õº∆¨
			content = this.getPageContent(link, tagName, attrName, attrValue);
			//—›‘±°¢µº—›µ»ΩÈ…‹
			attrValue = "movb34";
			content += this.getPageContent(link, tagName, attrName, attrValue);
			//æÁ«ÈΩÈ…‹
			attrValue = "movb42";
			content += this.getPageContent(link, tagName, attrName, attrValue);
			
			content = this.commonParseContent(content);
			

		} catch (Exception e) {
			System.out.println("Ω‚ŒˆMOVIE.PCPOP“≥√Ê[" + link + "]ƒ⁄»› ß∞‹");
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

	// TODO ≤‚ ‘”√
	public static void main(String[] args) {
		//http://movie.pcpop.com/197039_1_0_197.html ŒﬁΩÈ…‹
		//http://movie.pcpop.com/196957_1_0_196.html ¥¯ΩÈ…‹
		String url = "http://movie.pcpop.com/197039_1_0_197.html";
		ParsePcpopMovie p = new ParsePcpopMovie();
		String cnt =  p.parse(url);
		System.out.println(cnt);
	}
}
