/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.huanqiu;

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
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
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
			}
			page = this.getPageContent(link, "class", "pic");
			if(!"".equals(page)){
				log.debug("有分页");
				Parser parser = new Parser(link);
				parser.setEncoding("UTF-8");
				NodeFilter filter = new AndFilter(new TagNameFilter("p"), new HasAttributeFilter("class", "pg"));
				NodeList nodeList = parser.extractAllNodesThatMatch(filter);
				if(nodeList != null && nodeList.size() >0){
					NodeList childrenList = nodeList.elementAt(0).getChildren();
					Span span = null;
					for(NodeIterator iter = childrenList.elements(); iter.hasMoreNodes();){
						Node node = iter.nextNode();
						if(node instanceof Span){
							span = (Span)node;
							break;
						}
					}
					if(span != null){
						filter = new NodeClassFilter(LinkTag.class);
						NodeList linkTags = span.getChildren().extractAllNodesThatMatch(filter);
						for(NodeIterator iter = linkTags.elements(); iter.hasMoreNodes();){
							LinkTag linkTag = (LinkTag)iter.nextNode();
							String _link = linkTag.extractLink();
							links.add(_link);
						}
					}
				}
			}
			
			//	去除重复地址
			if(links.size() > 1){
				Iterator<String> iter = new UniqueFilterIterator(links.iterator());
				List<String> temp = new ArrayList<String>();
				while(iter.hasNext()){
					temp.add(iter.next());
				}
				if(!temp.isEmpty()){
					links.clear();
					links.addAll(temp);
				}
			}
			
			if(log.isDebugEnabled()){
				for(String _link : links){
					log.debug("分页地址:"+_link);
				}
			}
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
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
			if("".equals(content)){
				Parser parser = new Parser(link);
				parser.setEncoding("UTF-8");
				NodeFilter filter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "pic"));
				NodeList nodeList = parser.extractAllNodesThatMatch(filter);
				if(nodeList != null && nodeList.size() > 0) {
					content += nodeList.toHtml();
				}
			}
			
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
			//System.out.println("解析环球网页面["+link+"]内容失败");
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
		System.out.println("start...");
		String link="http://humor.huanqiu.com/life/singal/2009-03/399085.html";
		ParseHuanQiu huanqiu = new ParseHuanQiu();
		String content = huanqiu.parse(link);
		System.out.println(content);
		System.out.println("end...");
	}
}
