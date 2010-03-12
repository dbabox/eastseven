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
 *         create time : 2009-11-26 ����04:32:42<br>
 *         �ֻ���Ѷ
 */
public class ParseZolMobile extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseZolMobile.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			//System.out.println("ȡZOL.MOBILE[" + link + "]��ҳʧ��");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		try {
			content = this.getPageContent(link, "div", "id", "cotent_idd");
			content = this.commonParseContent(content);
			
			content = content.replaceAll("<br clear=\"all\" />", RegexUtil.REGEX_BR);
			content = content.replaceAll("<br/>", RegexUtil.REGEX_BR);
			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			content = RegexUtil.eliminateString(RegexUtil.REGEX_SCRIPT_START, RegexUtil.REGEX_SCRIPT_END, content);
			
		} catch (Exception e) {
			//System.out.println("����ZOL.MOBILEҳ��[" + link + "]����ʧ��");
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
		//http://mobile.zol.com.cn/more/2_426.shtml
		//http://mobile.zol.com.cn/156/1566229.html
		String url = "http://mobile.zol.com.cn/156/1566229.html";
		ParseZolMobile p = new ParseZolMobile();
		System.out.println(p.parse(url));
	}
}
