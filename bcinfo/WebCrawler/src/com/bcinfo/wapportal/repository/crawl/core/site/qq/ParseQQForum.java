/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.qq;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����01:51:34
 */
public class ParseQQForum extends AbstractHtmlParseTemplete implements Parse {

	private static final Logger log = Logger.getLogger(ParseQQForum.class);
	
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
			//��̳���Ӳ����ڷ�ҳ����
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			//System.out.println("ȡQQ��̳["+link+"]��ҳʧ��");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "main_content_div");
			content = this.commonParseContent(content);
			
		}catch(Exception e){
			//System.out.println("����QQ��̳ҳ��["+link+"]����ʧ��");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		
		return content;
	}

}
