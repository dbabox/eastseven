/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.sohu;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����01:54:47
 */
public class ParseSohu extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseSohu.class);
	
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
			System.out.println("ȡsohu["+link+"]��ҳʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "contentText");
			if(content == null || "".equals(content))
				content = this.getPageContent(link, "td", "class", "wz12_3B07");
				
			content = this.commonParseContent(content);
			
			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			content = content.replaceAll(RegexUtil.REGEX_EM, replacement);
			content = content.replaceAll("\\[����˵����\\]", replacement);
			content = RegexUtil.eliminateString(RegexUtil.REGEX_SCRIPT_START, RegexUtil.REGEX_SCRIPT_END, content);
		}catch(Exception e){
			System.out.println("����sohuҳ��["+link+"]����ʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		
		return content;
	}
}
