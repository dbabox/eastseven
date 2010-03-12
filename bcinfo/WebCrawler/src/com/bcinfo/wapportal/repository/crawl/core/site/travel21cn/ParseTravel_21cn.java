package com.bcinfo.wapportal.repository.crawl.core.site.travel21cn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**  
 * @author lvhs 	
 * E-mail:lvhs@flying-it.com  
 * @version 创建时间：2010-3-4 上午09:53:08  
 * 类说明  :抓取http://travel.21cn.com/guide/zijia/list1.shtml
 */
public class ParseTravel_21cn extends AbstractHtmlParseTemplete implements Parse {
	private static Logger log=Logger.getLogger(ParseTravel_21cn.class);
	
	public ParseTravel_21cn() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links=null;
		try{
			links=new ArrayList<String>();
			links.add(link);
			String page=this.getPageContent(link, "div","id","text");
			if(!"".equals(page)){
				Parser parser=new Parser(link);
				parser.setEncoding("gb2312");
				
				NodeFilter filter=new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","page"));
				NodeList nodeList=parser.extractAllNodesThatMatch(filter);
				if(nodeList!=null && nodeList.size()>0){
					for(NodeIterator iter=nodeList.elements();iter.hasMoreNodes();){
						Div node=(Div)iter.nextNode();
						if(node!=null){
							filter=new NodeClassFilter(LinkTag.class);
							if(node.getChildCount()>0){
								NodeList nodelist=node.getChildren().extractAllNodesThatMatch(filter);
								if(nodelist!=null){
									for(NodeIterator it=nodelist.elements();it.hasMoreNodes();){
										LinkTag linktag=(LinkTag)it.nextNode();
										String _link=linktag.extractLink();
										if(_link!=null){
											links.add(_link);
										}
									}
								}
							}
						}
					}
				}
			}
			
			if(links.size()>1){
				Iterator<String> iterator=new UniqueFilterIterator(links.iterator());
				List<String> ls=new ArrayList<String>();
				while(iterator.hasNext()){
					ls.add(iterator.next());
				}
				if(!ls.isEmpty()){
					links.clear();
					links.addAll(ls);
				}
			}
			
			if(log.isDebugEnabled()){
				for(String _links:links){
					log.debug("当前分页："+_links);
				}
			}
			
		}catch(Exception e){
			links=new ArrayList<String>();
			links.add(link);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content="";
		
		try{
			Parser parser=new Parser(link);
			parser.setEncoding("gb2312");
			NodeFilter filter=new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("id","text"));
			NodeList nodeList=parser.extractAllNodesThatMatch(filter);
			
			if(nodeList!=null && nodeList.size()>0){
				for(NodeIterator iter=nodeList.elements();iter.hasMoreNodes();){
					Div div=(Div)iter.nextNode();
					content=div.toHtml();
				}
			}
			
			if(!"".equals(content)){
				content=this.commonParseContent(content);
				content=content.replaceAll("                        	                        	", replacement);
				content=content.replaceAll("                        	                                                    ", replacement);
			}
		}catch(Exception e){
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
			
	
		return content;
	
	
	}

	@Override
	public Boolean parse(String folderId, String title, String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parse(String link) {
		// TODO Auto-generated method stub
		return this.simpleParse(link);
	}

	
	
	public static void main(String[] args) {
		String link="http://travel.21cn.com/guide/zijia/2009/10/13/6966069.shtml";
//		String link="http://travel.21cn.com/scene/sw/2010/03/04/7381282.shtml";		//flash图片无法获得图片地址
		ParseTravel_21cn pt=new ParseTravel_21cn();
		System.out.println(pt.parse(link));
	}

}
