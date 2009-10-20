/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.tom;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����01:56:04
 */
public class ParseTom extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseTom.class);
	
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
			if(link.contains("yule.tom.com")){
				log.debug("   ������һҳ�Ƿ��з�ҳ��ǩ "+link);
				//������һҳ�Ƿ��з�ҳ��ǩ
				String content = this.getPageContent(link, "id", "content_body");
				content = this.commonParseContent(content);
				if(content.contains("<option")){
					String options = RegexUtil.extractContentWithRegex("<option\\s+[^>]+>", content);
					String[] tmp = options.split("<option");
					for(int i=0;i<tmp.length;i++){
						String var = tmp[i].trim();
						if(!var.contains("selected")&&var.length()>0){
							String pageLink = link.substring(0, link.lastIndexOf("/")+1) + var.substring(var.indexOf("'")+1,var.lastIndexOf("'"));
							links.add(pageLink);
							log.debug("   ������һҳ�з�ҳ��ǩ "+pageLink);
						}
					}
				}else{
					log.debug("   û�з�ҳ��ǩ");
				}
			}else if(link.contains("images.yule.tom.com")){
				//TODO ��ʱ������
				//http://images.yule.tom.com/vw/158532-1.html
			}else{
				log.debug("   û�з�ҳ��ǩ");
			}
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("ȡTOM["+link+"]��ҳʧ��");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "content_body");
			content = this.commonParseContent(content);
			
			content = content.replaceAll("��������ͼ��\\d+�ţ�", replacement);
			content = content.replaceAll("[����ͼ��]", replacement);
			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			content = content.replaceAll(RegexUtil.REGEX_IFRAME, replacement);
			content = content.replaceAll("\\[\\]", replacement);
			content = content.replaceAll("\\(\\)", replacement);
			content = content.replaceAll("\\��\\��", replacement);
			content = content.replaceAll("\\(��ࣺ.*\\)", replacement);
			content = content.replaceAll("<MUTILPAGENAV>", replacement);
			//ȥ����ҳ��ǩ
			content = RegexUtil.eliminateString(RegexUtil.REGEX_SELECT_START, RegexUtil.REGEX_SELECT_END, content);
		}catch(Exception e){
			System.out.println("����TOMҳ��["+link+"]����ʧ��");
		}
		
		return content;
	}

}
