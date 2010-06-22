/**
 * 
 */
package com.bcinfo.crawl.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.bcinfo.crawl.domain.model.Channel;
import com.bcinfo.crawl.domain.model.Resource;
import com.bcinfo.crawl.domain.model.Site;

/**
 * @author dongq
 * 
 *         create time : 2010-5-23 上午11:52:06
 */
public final class CrawlerUtil {

	private static final Log log = LogFactory.getLog(CrawlerUtil.class);
	
	public static String getCharset(String link) {
		String charset = "GBK";
		
		try {
			Parser parser = new Parser(link);
			Lexer lexer = parser.getLexer();
			Node node = null;
			while((node = lexer.nextNode())!=null){
				if(node instanceof MetaTag) {
					MetaTag meta = (MetaTag)node;
					String value = meta.getAttribute("content");
					if(value!=null&&!"".equals(value)&&value.toLowerCase().contains("charset")){
						charset = value.substring(value.lastIndexOf("=")+1);
					}
				}
			}
			
		} catch (Exception e) {
			log.warn("获取页面" + link + "的字符集编码失败，将使用默认的GBK编码");
			log.warn(e.getMessage());
		}
		
		return charset.toUpperCase();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Site> getSites(InputStream input) {
		List<Site> sites = null;
		
		try {
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(input);
			Element root = doc.getRootElement();
			List siteList = root.getChildren("site");
			sites = new ArrayList<Site>();
			for(Iterator iter = siteList.iterator(); iter.hasNext(); ) {
				Site site = new Site();
				Element siteElement = (Element)iter.next();
				site.setChannelId(Long.parseLong(siteElement.getChild("channelId").getText()));
				site.setChannelName(siteElement.getChild("channelName").getText());
				site.setCharset(siteElement.getChild("charset").getText());
				site.setUrl(siteElement.getChild("url").getText());
				site.setPageSuffix(siteElement.getChild("pageSuffix").getText());
				site.setRealTime(Boolean.valueOf(siteElement.getChild("realTime").getText()));
				sites.add(site);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sites;
	}
	
	public static List<Resource> getResources(Site site) {
		List<Resource> resources = null;
		
		try {
			
			Parser parser = new Parser(site.getUrl());
			parser.setEncoding(site.getCharset());
			NodeList nodeList = parser.extractAllNodesThatMatch(new TagNameFilter("a"));
			if(nodeList != null && nodeList.size() > 0) {
				resources = new ArrayList<Resource>();
				for(NodeIterator iter = nodeList.elements(); iter.hasMoreNodes(); ) {
					LinkTag linkTag = (LinkTag)iter.nextNode();
					String title = linkTag.getLinkText().trim();
					String link = linkTag.extractLink();
					
					if(link.isEmpty() || title.isEmpty()) continue;
					if(title.length() <= 2) continue;
					if(title.contains("+") || title.contains("&gt;")) continue;
					if(!link.endsWith(site.getPageSuffix())) continue;
					Resource resource = new Resource(link, title, site.getCharset());
					resource.setChannel(new Channel(site.getChannelId(), site.getChannelName()));
					resources.add(resource);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resources;
	}
	
	public static String removeTagContents(String content, List<Map<String, String>> tags) {
		String cnt = "";
		try {
			cnt = content;
			for(Map<String, String> tag : tags) {
				String tagStart = (String)tag.get("tagStart");
				String tagEnd = (String)tag.get("tagEnd");
				cnt += removeTagContent(cnt, tagStart, tagEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return content;
		}
		return cnt;
	}
	
	public static String removeTagContent(String content, String tagStart, String tagEnd) {
		String cnt = "";
		final String replacement = "";
		try {
			cnt = content;
			String text = StringUtils.substringBetween(cnt, tagStart, tagEnd);
			if(StringUtils.isNotEmpty(text)) cnt = cnt.replace(text, replacement).replaceAll(tagStart, replacement).replaceAll(tagEnd, replacement);
			
		} catch (Exception e) {
			e.printStackTrace();
			return content;
		}
		return cnt;
	}
}
