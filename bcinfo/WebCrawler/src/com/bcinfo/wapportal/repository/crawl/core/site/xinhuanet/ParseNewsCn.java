/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.xinhuanet;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-26 下午05:47:36
 */
public class ParseNewsCn extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseNewsCn.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
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
			//www.news.cn的内容是包含在class=txt的table标签内的
			Parser parser = new Parser(link);
			parser.setEncoding("GBK");
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(TableTag.class));
			if(nodeList!=null&&nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					TableTag tableTag = (TableTag)iter.nextNode();
					if("txt".equals(tableTag.getAttribute("class")))
						content = tableTag.toHtml();
				}
			}
			if(content!=null){
				content = this.commonParseContent(content);
				content = content.replaceAll(RegexUtil.REGEX_TABLE_ALL, replacement);
				content = content.replaceAll(RegexUtil.REGEX_STRONG, replacement);
				content = content.replaceAll(RegexUtil.REGEX_P_END, replacement);
				content = content.replaceAll(RegexUtil.REGEX_P_START, RegexUtil.REGEX_BR);
				content = content.replaceAll(RegexUtil.REGEX_P_START_NO_ATTR, RegexUtil.REGEX_BR);
				content = content.replaceAll("&nbsp;", " ");
			}
		}catch(Exception e){
			//System.out.println("解析新华网页面["+link+"]内容失败");
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

}
