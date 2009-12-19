/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.DebugUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-11-3 上午11:45:20
 */
public class ParsePcladyAstro extends AbstractHtmlParseTemplete implements
		Parse {

	private static Logger log = Logger.getLogger(ParsePcladyAstro.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
			
			String content = this.getPageContent(link, "id", "artNav");
			if(!"".equals(content)){
				Parser parser = new Parser();
				parser.setInputHTML(content);
				NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
				
				if(log.isDebugEnabled()) DebugUtil.printNodeList(nodeList);
				
				if(nodeList!=null && nodeList.size()>0){
					NodeIterator iter = nodeList.elements();
					while(iter.hasMoreNodes()){
						LinkTag linkTag = (LinkTag)iter.nextNode();
						if(!link.equals(linkTag.extractLink()))
							links.add(linkTag.extractLink());
					}
				}
			}
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			//System.out.println("取ASTRO.PCLADY[" + link + "]分页失败");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {
			content = this.getPageContent(link, "class", "words");
			Parser parser = new Parser();
			parser.setInputHTML(content);
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(TableTag.class));
			if(nodeList!=null && nodeList.size()>0){
				 Node node = nodeList.elementAt(0);
				 content = node.toHtml();
			}
			
			content = this.commonParseContent(content);
			
			content = content.replaceAll("点击图片进入下一页", replacement);
			content = content.replaceAll("&ldquo;", "“");
			content = content.replaceAll("&rdquo;", "”");
			content = content.replaceAll("&mdash;", "——");
			content = content.replaceAll("PClady独家专稿 未经许可请勿转载！", replacement);
			content = content.replaceAll("\\(图片来源：PConline摄影部落\\)", replacement);
		} catch (Exception e) {
			//System.out.println("解析ASTRO.PCLADY页面[" + link + "]内容失败");
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
		String url = "http://astro.pclady.com.cn/horoscopes/love/lovers/0910/464515.html";
		ParsePcladyAstro p = new ParsePcladyAstro();
		String cnt =  p.parse(url);
		//System.out.println(cnt);
	}
}
