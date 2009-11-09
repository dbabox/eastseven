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
 *         create time : 2009-11-3 ����11:21:08
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
			System.out.println("ȡMOVIE.PCPOP[" + link + "]��ҳʧ��");
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
			
			//ͼƬ
			content = this.getPageContent(link, tagName, attrName, attrValue);
			//��Ա�����ݵȽ���
			attrValue = "movb34";
			content += this.getPageContent(link, tagName, attrName, attrValue);
			//�������
			attrValue = "movb42";
			content += this.getPageContent(link, tagName, attrName, attrValue);
			
			content = this.commonParseContent(content);
			

		} catch (Exception e) {
			System.out.println("����MOVIE.PCPOPҳ��[" + link + "]����ʧ��");
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

	// TODO ������
	public static void main(String[] args) {
		//http://movie.pcpop.com/197039_1_0_197.html �޽���
		//http://movie.pcpop.com/196957_1_0_196.html ������
		String url = "http://movie.pcpop.com/197039_1_0_197.html";
		ParsePcpopMovie p = new ParsePcpopMovie();
		String cnt =  p.parse(url);
		System.out.println(cnt);
	}
}
