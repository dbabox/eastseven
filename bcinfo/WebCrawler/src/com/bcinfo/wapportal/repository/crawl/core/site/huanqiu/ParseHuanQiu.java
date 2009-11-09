/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.huanqiu;

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
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-29 下午02:54:37
 */
public class ParseHuanQiu extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseHuanQiu.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
			//分页<div class="contentpage">
			String page = this.getPageContent(link, "div", "class", "contentpage");
			if(!"".equals(page)){
				//分页标签有上下两个，只去其中的一个即可
				Parser parser = new Parser();
				parser.setInputHTML(page);
				NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
				if(nodeList!=null){
					NodeIterator iter = nodeList.elements();
					List<String> list = new ArrayList<String>();
					while(iter.hasMoreNodes()){
						LinkTag linkTag = (LinkTag)iter.nextNode();
						list.add(linkTag.extractLink());
					}
					for(int index=0;index<list.size();index++){
						String _link = list.get(index);
						if(links.contains(_link))
							list.remove(index);
						else
							links.add(_link);
					}
					
				}
				if(log.isDebugEnabled()){
					for(String pageLink : links){
						System.out.println(link+" 包含的分页地址： "+pageLink);
					}
				}
			}
			
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("取环球网["+link+"]分页失败");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		try{
			content = this.getPageContent(link, "div", "id", "text");
			content = this.commonParseContent(content);
			
			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			content = content.replaceAll(RegexUtil.REGEX_SCRIPT_START, replacement);
			content = content.replaceAll(RegexUtil.REGEX_SCRIPT_END, replacement);
			content = content.replaceAll(RegexUtil.REGEX_EMBED, replacement);
			content = content.replaceAll("\\d+?\\[下一页\\]", replacement);
			content = content.replaceAll("\\[上一页\\]?\\d+", replacement);
			content = content.replaceAll("\\[下一页\\]", replacement);
			content = content.replaceAll("\\[上一页\\]", replacement);
		}catch(Exception e){
			System.out.println("解析环球网页面["+link+"]内容失败");
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

	public static void main(String[] args) {
		//http://world.huanqiu.com/roll/2009-10/616902.html
		String link = "http://world.huanqiu.com/roll/2009-10/616629.html";
		ParseHuanQiu huanqiu = new ParseHuanQiu();
		String content = huanqiu.parse(link);
		System.out.println(content);
	}
}
