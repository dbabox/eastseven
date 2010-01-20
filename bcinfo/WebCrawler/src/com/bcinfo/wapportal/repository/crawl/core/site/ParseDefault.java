/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.log4j.Logger;
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

/**
 * @author dongq
 * 
 *         create time : 2009-12-9 下午01:09:45
 */
public class ParseDefault extends AbstractHtmlParseTemplete implements Parse {

	private static final Logger log = Logger.getLogger(ParseDefault.class);

	private Parser parser;
	private Map<String, String> map;
	
	/**
	 * 
	 * @param map[tagName,attrName,attrValue]
	 */
	public ParseDefault(Map<String, String> map) {
		parser = new Parser();
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
			String name = this.getPageName(link);
			parser.setURL(link);
			NodeFilter filter = new TagNameFilter("a");
			NodeList nodeList = parser.extractAllNodesThatMatch(filter);
			if(nodeList!=null&&nodeList.size()>0){
				String pageLink = "";
				for(NodeIterator iter = nodeList.elements();iter.hasMoreNodes();){
					LinkTag linkTag = (LinkTag)iter.nextNode();
				 	pageLink = linkTag.extractLink();
				 	if(pageLink.contains(name)&&!pageLink.contains(link)){
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
			
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			log.info("取[" + link + "]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {
			parser.reset();
			parser.setURL(link);
			NodeFilter filter = new AndFilter(new TagNameFilter(map.get(TAG_NAME)), new HasAttributeFilter(map.get(ATTRIBUTE_NAME), map.get(ATTRIBUTE_VALUE)));
			NodeList nodeList = parser.extractAllNodesThatMatch(filter);
			if(nodeList!=null&&nodeList.size()>0){
				content = nodeList.toHtml();
				content = this.commonParseContent(content);
			}
		} catch (Exception e) {
			log.info("解析页面[" + link + "]内容失败");
			if (log.isDebugEnabled())
				e.printStackTrace();
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

	// TODO TEST
	public static void main(String[] args) {
		//http://news.xinhuanet.com/photo/2010-01/05/content_12756132.htm
		String link = "http://www.china.com.cn/photochina/2010-01/14/content_19234280.htm";
		Map<String, String> map = new HashMap<String, String>();
		map.put(TAG_NAME, "div");
		map.put(ATTRIBUTE_NAME, "id");
		map.put(ATTRIBUTE_VALUE, "artibody");
		ParseDefault p = new ParseDefault(map);
		//http://www.mtime.com/news/2010/01/11/1422898.html
		//mt15 px14 lh15
		try {
			System.out.println(p.parse(link));
			System.out.println(" ");
			map = new HashMap<String, String>();
			map.put(TAG_NAME, "div");
			map.put(ATTRIBUTE_NAME, "class");
			map.put(ATTRIBUTE_VALUE, "mt15 px14 lh15");
			System.out.println(new ParseDefault(map).parse("http://www.mtime.com/news/2010/01/11/1422898.html"));
			//System.out.println(p.checkPageOfLinks(link));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
