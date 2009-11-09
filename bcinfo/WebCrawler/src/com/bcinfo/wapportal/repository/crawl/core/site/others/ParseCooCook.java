/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-11-5 ����09:01:36
 */
public class ParseCooCook extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseCooCook.class);

	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("ȡCooCook[" + link + "]��ҳʧ��");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {
			content = this.getPageContent(link, "id", "articlebody");
			if(content != null && !"".equals(content)){
				
			}else{
				content = this.getPageContent(link, "id", "xspace-showmessage");
			}
			content = this.commonParseContent(content);

			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			content = content.replaceAll("<\\?xml:namespace\\s+[^>]+/>", replacement);
			content = content.replaceAll("<o:p>|</o:p>", replacement);
			content = content.replaceAll("<[bB][rR]\\s+[^>]+>", RegexUtil.REGEX_BR);
			content = content.replaceAll(RegexUtil.REGEX_SCRIPT_START, replacement);
			content = content.replaceAll(RegexUtil.REGEX_SCRIPT_END, replacement);
			content = content.replaceAll(RegexUtil.REGEX_COMMENT, replacement);
			
			content = content.replaceAll("�����ʳר��.*ʳ������", "shipuyongliao");
			content = content.replaceAll("shipuyongliao", "ʳ������");
			content = content.replaceAll("��������Ϣ", replacement);
			content = content.replaceAll("�ղ���ƪ���ף��������ղص��ҵĸ��˿ռ���", replacement);
			content = content.replaceAll("�༭�Ƽ�.*?����", replacement);
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

	public static void main(String[] args) {
		String link = "http://www.coocook.com/index.php/action_viewnews_itemid_65295.html";
		ParseCooCook p = new ParseCooCook();
		System.out.println(p.parse(link));
	}
}
