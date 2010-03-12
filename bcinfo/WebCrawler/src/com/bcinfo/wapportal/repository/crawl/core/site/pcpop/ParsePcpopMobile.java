/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.pcpop;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-11-27 下午01:49:34
 */
public class ParsePcpopMobile extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParsePcpopMobile.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
			
			Parser parser = new Parser(link);
			parser.setEncoding("GBK");
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			if(nodeList != null && nodeList.size() > 0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					LinkTag linkTag = (LinkTag)iter.nextNode();
					String title = linkTag.getLinkText();
					if("在本页阅读全文".equals(title)){
						links.add(linkTag.extractLink());
						links.remove(link);
					}
				}
			}
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			if(log.isDebugEnabled()) e.printStackTrace();
			log.info("取MOBILE.PCPOP[" + link + "]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {
			content = this.getPageContent(link, "div", "id", "contentDiv");
			if("".equals(content)){
				content = this.getPageContent(link, "div", "class", "pop_2_1_5");
			}
			content = this.commonParseContent(content);
			content = content.replaceAll("<[sS][cC][rR][iI][pP][tT]>.*</[sS][cC][rR][iI][pP][tT]>", replacement);
			
			content = content.replaceFirst("泡泡网手机频道", replacement);
		} catch (Exception e) {
			//System.out.println("解析MOBILE.PCPOP页面[" + link + "]内容失败");
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

	// TODO 测试用
	public static void main(String[] args) {
		//http://www.pcpop.com/doc/0/468/468713.shtml 分页A[在本页阅读全文]
		//http://www.pcpop.com/doc/0/466/466595.shtml 分页B[第二页内容是参数信息，不管]
		//http://www.pcpop.com/doc/0/468/468094.shtml
		String link = "http://www.pcpop.com/doc/0/468/468094.shtml";
		ParsePcpopMobile p = new ParsePcpopMobile();
		System.out.println(p.parse(link));
	}
}
