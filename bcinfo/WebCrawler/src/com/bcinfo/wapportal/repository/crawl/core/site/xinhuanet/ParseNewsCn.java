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
 *         create time : 2009-10-26 ����05:47:36
 */
public class ParseNewsCn extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseNewsCn.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("ȡ�»���["+link+"]��ҳʧ��");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			
		}catch(Exception e){
			System.out.println("�����»���ҳ��["+link+"]����ʧ��");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
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

}
