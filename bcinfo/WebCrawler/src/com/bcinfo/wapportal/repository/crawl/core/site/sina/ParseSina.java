/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.sina;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����01:53:52
 */
public class ParseSina extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseSina.class);
	
	@Override
	public String parse(String link) {
		return this.simpleParse(link);
	}
	
	@Override
	public Boolean parse(String folderId, String title, String link) {
		Boolean bln = false;
		
		try{
			//ץȡ����
			
			//�ֶδ���
			
			//�������
		}catch(Exception e){
			log.info("ҳ��["+link+"]["+title+"]����ʧ��");
			if(log.isDebugEnabled()) log.error(e);
		}
		
		return bln;
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
			//System.out.println("ȡsina["+link+"]��ҳʧ��");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "artibody");
			////System.out.println(content);
			content = this.commonParseContent(content);
			//System.out.println(content);
			//�����а����ű���ǩ
			if(content.contains("<style") 
					|| content.contains("<span") 
					|| content.contains("<script")
					|| content.contains("<script language=\"javascript\" type=\"text/javascript\">")){
				
				content = content.replaceAll("����<span class=\"f_c00\">_COUNT_</span>������</span>��Ҫ����", replacement);
				content = RegexUtil.replaceAll("<[sS][cC][rR][iI][pP][tT]\\s+[^>]+>.*?</[sS][cC][rR][iI][pP][tT]>", content);
				content = RegexUtil.replaceAll("<[sS][tT][yY][lL][eE]\\s+[^>]+>.*?</[sS][tT][yY][lL][eE]>", content);
				content = RegexUtil.replaceAll("<[sS][tT][yY][lL][eE]>.*?</[sS][tT][yY][lL][eE]>", content);
				content = RegexUtil.replaceAll("<[sS][pP][aA][nN]\\s+[^>]+>.*?</[sS][pP][aA][nN]>", content);
				
				content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			}
			content = content.replaceAll("\\(\\)", replacement);
			
			//��ʽ��
			/**/
			content = content.replaceAll(RegexUtil.REGEX_ENTER, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ENTER_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ESC_SPACE, replacement);
			content = content.replaceAll(RegexUtil.REGEX_FORMAT_SPACE, replacement);
		}catch(Exception e){
			//System.out.println("����sinaҳ��["+link+"]����ʧ��");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		
		return content;
	}

	//TODO TEST
	public static void main(String[] args) {
		//System.out.println("start");
		String link = "http://tech.sina.com.cn/d/2009-12-03/10003645879.shtml";
		ParseSina p = new ParseSina();
		//System.out.println(p.parse(link));
		//System.out.println("done");
	}
}
