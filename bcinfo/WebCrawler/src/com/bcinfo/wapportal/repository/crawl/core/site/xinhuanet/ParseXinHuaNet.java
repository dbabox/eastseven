/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.xinhuanet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 下午01:56:56
 */
public class ParseXinHuaNet extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseXinHuaNet.class);
	
	private Parser parser;
	
	public ParseXinHuaNet() {
		this.parser = new Parser();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
			
			//String name = this.getPageName(link);
			parser.setURL(link);
			parser.setEncoding("GB2312");
			NodeFilter filter = new AndFilter(new TagNameFilter("p"), new HasAttributeFilter("class", "pagelink"));
			NodeList nodeList = parser.extractAllNodesThatMatch(filter);
			if(nodeList!=null&&nodeList.size()>0){
				String pageLink = "";
				Node node = nodeList.elements().nextNode();
				for(NodeIterator iter = node.getChildren().elements();iter.hasMoreNodes();){
					Node _node = iter.nextNode();
					if(_node instanceof LinkTag) {
						LinkTag linkTag = (LinkTag)_node;
						pageLink = linkTag.extractLink();
						if(link.equals(pageLink)) continue;
						links.add(pageLink);
					}
				}
				if(!links.isEmpty()){
					Iterator<String> iter = new UniqueFilterIterator(links.iterator());
					links = new ArrayList<String>();
					while(iter.hasNext()){
						links.add(iter.next());
					}
				}
			}
			parser.reset();
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			//System.out.println("取新华网["+link+"]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "class", "xilanwz-x");
			if("".equals(content)){
				content = this.getPageContent(link, "id", "Content");
				/*
				parser.setURL(link);
				NodeList nodeList = parser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "Content")));
				if(nodeList!=null&&nodeList.size()>0){
					Node[] nodes = nodeList.toNodeArray();
					for(Node node : nodes){
						System.out.println(node.getClass()+" : "+node);
					}
				}
				*/
			}
			if("".equals(content)){
				content = this.getPageContent(link, "id", "xhw");
			}
			if("".equals(content)){
				content = this.getPageContent(link, "td", "id", "xhw");
			}
			content = this.commonParseContent(content);
			
			//新华网
			content = content.replaceAll("\\[进入.*论坛\\]", replacement);
			content = content.replaceAll("发表您的观点。请您文明上网、理性发言并遵守相关规定，在注册后发表评论。  留言须知", replacement);
			content = content.replaceAll("<img src=\"http://imgs.xinhuanet.com/icon/newscenter/news_hy.gif\"  border=\"0\">", replacement);
			content = content.replaceAll("<img src=\"http://imgs.xinhuanet.com/icon/newscenter/news_xy.gif\"  border=\"0\">", replacement);
			content = content.replaceAll("<img src=\"http://imgs.xinhuanet.com/icon/newscenter/news_sy.gif\"  border=\"0\">", replacement);
			content = content.replaceAll("<object\\s+[^>]+>|</object>|<param\\s+[^>]+>|</param>|<embed\\s+[^>]+>|</embed>", replacement);
			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			//分页标签:[1][2][3][4]
			content = content.replaceAll("\\[\\d+\\]", replacement);
			if(log.isDebugEnabled()){
				////System.out.println("------------------------"+link+"-------------------------------");
				////System.out.println(content);
			}
		}catch(Exception e){
			//System.out.println("解析新华网页面["+link+"]内容失败");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		 
		return content;
	}

	public static void main(String[] args) {
		//http://news.xinhuanet.com/photo/2010-01/04/content_12751026.htm
		String link = "http://news.xinhuanet.com/photo/2010-01/13/content_12800300.htm";
		ParseXinHuaNet p = new ParseXinHuaNet();
		System.out.println(p.parse(link));
	}
}
