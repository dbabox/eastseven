package com.bcinfo.wapportal.repository.crawl.core.site.others;

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
import org.htmlparser.lexer.Lexer;
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
 * @version 创建时间：2010-3-8 上午11:26:31  
 * 类说明  :抓取cocplay网站动漫图片(http://www.cocplay.com/list.php?fid=1)
 */
public class ParseCocPlay extends AbstractHtmlParseTemplete implements Parse{
	
	private static Logger log=Logger.getLogger(ParseCocPlay.class);
	
	public ParseCocPlay() {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links=null;
		try{
			links=new ArrayList<String>();
			links.add(link);
			
			String page=this.getPageContent(link, "td","class","wztext2");
			if(!"".equals(page)){
				Parser parser=new Parser(link);
				parser.setEncoding("gb2312");
				NodeFilter filter=new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("id","nexttext"));
				NodeList nodeList=parser.extractAllNodesThatMatch(filter);
				if(nodeList!=null && nodeList.size()>0){
					for(NodeIterator iters=nodeList.elements();iters.hasMoreNodes();){
						Div node=(Div)iters.nextNode();
//						log.debug("-------:"+node.toHtml().trim());
						if(node!=null){
							filter=new NodeClassFilter(LinkTag.class);
							if(node.getChildCount()>0){
								NodeList new_nodeList=node.getChildren().extractAllNodesThatMatch(filter);
								if(new_nodeList!=null && new_nodeList.size()>0){
									for(NodeIterator it_er=new_nodeList.elements();it_er.hasMoreNodes();){
										LinkTag link_tag=(LinkTag)it_er.nextNode();
										if(link_tag!=null){
											String link_ss=link_tag.extractLink();
											//log.debug("---------:"+link_ss);
											if(link_ss!=null){
												links.add(link_ss);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			List<String> list=new ArrayList<String>();;
			if(links.size()>1){
				Iterator<String> it=new UniqueFilterIterator(links.iterator());
				List<String> ls=new ArrayList<String>();
				while(it.hasNext()){
					ls.add(it.next());
				}
				if(!ls.isEmpty()){
					links.clear();
					links.addAll(ls);
				}
			}
			
			String endHref=links.get(links.size()-1);
			String[] temp={};
			temp=endHref.split("page=");
			String url_head=null;
			url_head=temp[0];
			if(temp.length==2){
				endHref=temp[1]; 
			}
			
			int length=0;
			if(endHref!=null && !"".equals(endHref.trim()) && !"".equals(null)){
			    length=Integer.parseInt(endHref);
			}
			
			String http_url="";
			if(length>1){
				for(int k=1;k<=length;k++){
					http_url=url_head+"page="+k;
					if(http_url!=null){
						list.add(http_url);
						links.clear();
						links.addAll(list);
					}
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
			NodeList nodeList=parser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("td"),new HasAttributeFilter("class","wztext2")));
			
			if(nodeList!=null && nodeList.size()>0){
				for(NodeIterator iter=nodeList.elements();iter.hasMoreNodes();){
					Node node=iter.nextNode();
					content+=node.toHtml().trim();
				}
			}
			
			parser.reset();
			parser.setInputHTML(content);
			Lexer lexer = parser.getLexer();
			Node node = null;
			content = "";
			while ((node = lexer.nextNode()) != null) {
//				 log.debug(":::::::"+node.toHtml());
//				 log.debug("~~~~~~~~"+node.getClass());
//				 log.debug("------------------------");
				 if (node instanceof ImageTag && node.toHtml().trim().contains("onload")) {
//					 log.debug("========:"+node.toHtml());
//					 log.debug("-------"+node.getText());
				    String  no_de=node.getText();
					if(no_de.contains("onload='if(this.width>600)makesmallpic(this,600,1800);'")){
						String[] ss={};
						ss=no_de.split(";'");
						if(ss.length==2){
						no_de=ss[1];
						}
					}
					content += no_de;
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
//		String link="http://www.cocplay.com/bencandy.php?fid=1&aid=1766&page=1";
		
		String link="http://www.cocplay.com/bencandy.php?fid=1&id=1732";
		ParseCocPlay pcp=new ParseCocPlay();
		System.out.println(pcp.parse(link));

	}

}
