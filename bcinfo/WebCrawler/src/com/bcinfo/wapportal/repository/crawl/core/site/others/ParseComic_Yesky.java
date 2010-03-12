package com.bcinfo.wapportal.repository.crawl.core.site.others;

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
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**  
 * @author lvhs 	
 * E-mail:lvhs@flying-it.com  
 * @version 创建时间：2010-3-8 下午05:04:50  
 * 类说明  :抓取天极网网站动漫图片(http://comic.yesky.com/more/443_4239_dmxwtxt_1.shtml)
 */
public class ParseComic_Yesky  extends AbstractHtmlParseTemplete implements Parse{
	
	private static Logger log=Logger.getLogger(ParseComic_Yesky.class);
	
	public ParseComic_Yesky() {
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

	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links=null;
		String httpHeader = null;
		try{
			links=new ArrayList<String>();
			httpHeader = link.substring(0, link.lastIndexOf("/")+1);
			links.add(link);
			Parser parser=new Parser(link);
			parser.setEncoding("gb2312");
			String page=this.getPageContent(link,"div","class","ArticleCnt");
			if(!"".equals(page)){
				NodeFilter filter=new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("id","numpage") );
				NodeList nodeList=parser.extractAllNodesThatMatch(filter);
				if(nodeList!=null && nodeList.size()>0){
					for(NodeIterator it_er=nodeList.elements();it_er.hasMoreNodes();){
						Div node=(Div)it_er.nextNode();
						if(node!=null){
							filter=new NodeClassFilter(LinkTag.class);
							if(node.getChildCount()>0){
								NodeList new_nodeList=node.getChildren().extractAllNodesThatMatch(filter);
								if(new_nodeList!=null && new_nodeList.size()>0){
									for(NodeIterator it=new_nodeList.elements();it.hasMoreNodes();){
										LinkTag _link_tag=(LinkTag)it.nextNode();
										if(_link_tag!=null){
											String _link_ss=_link_tag.extractLink();
											if(!_link_ss.startsWith("http://")){
												_link_ss = httpHeader + _link_ss;
											}
											links.add(_link_ss);
										}
									}
								}
							}
						}
					}
				}
			}
			
			if(links.size()>1){
				Iterator<String> it_e=new UniqueFilterIterator(links.iterator());
				List<String> lis=new ArrayList<String>();
				while(it_e.hasNext()){
					lis.add(it_e.next());
				}
				if(!lis.isEmpty()){
					links.clear();
					links.addAll(lis);
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
			
			NodeFilter filter=new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","ArticleCnt"));
			NodeList nodeList=parser.extractAllNodesThatMatch(filter);
			if(nodeList!=null && nodeList.size()>0){
				for(NodeIterator it_ers=nodeList.elements();it_ers.hasMoreNodes();){
					Div node=(Div)it_ers.nextNode();
					content+=node.toHtml().trim().trim();
				}
			}
			
		content=this.commonParseContent(content);
			
		}catch(Exception e){
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		
		return content;
	}

	
	
	public static void main(String[] args) {
		String link="http://comic.yesky.com/265/11139265.shtml";
//		String link="http://comic.yesky.com/395/11159395.shtml";
		ParseComic_Yesky pc=new ParseComic_Yesky();
		System.out.println(pc.parse(link));

	}

}
